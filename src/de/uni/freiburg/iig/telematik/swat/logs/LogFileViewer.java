package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.file.FileUtils;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import de.invation.code.toval.file.MonitoredInputStream;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.AbstractLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Objects;

/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link ViewComponent}
 *
 */
public class LogFileViewer extends JScrollPane implements ViewComponent {

        private static final long serialVersionUID = 7051631037013916120L;
        
        protected static final long SWAT_FILE_TOO_BIG_TO_SHOW_SIZE = 2097152l;
        
        private JComponent properties = null;
        private JButton analyzeButton = null;
        private JComponent mainComponent;
        private final JProgressBar bar = new JProgressBar();

        private LogModel model;
        private ISciffLogReader logReader = null;
        private AbstractLogParser p;

        public LogFileViewer(LogModel model) throws Exception {
                this.model = model;
        }

        public LogModel getModel() {
                return model;
        }

        public void setModel(LogModel model) {
                this.model = model;
        }

        @Override
        public JComponent getMainComponent() {
                try {
                        mainComponent = getEditorField();
                        getViewport().add(mainComponent);
                        mainComponent.revalidate();
                        mainComponent.repaint();
                } catch (MalformedURLException e) {
                        Workbench.errorMessage("Could not generate log viewer, URL malformed", e, true);
                } catch (IOException e) {
                        Workbench.errorMessage("Could not generate log viewer, I/O Error", e, true);
                }
                return this;
        }

        private JComponent getEditorField() throws MalformedURLException, IOException {
                if (model.getFileReference().length() > SWAT_FILE_TOO_BIG_TO_SHOW_SIZE) {
                        if (model.getFileReference().getName().toLowerCase().endsWith(".mxml")) {
                                p = new MXMLLogParser();
                        } else if (model.getFileReference().getName().toLowerCase().endsWith(".xes")) {
                                p = new XESLogParser();
                        } else {
                                // TODO large aristaflow log support
                        }
                        return getParserPanel(p, getFileReference(), getFileReference().length());
                } else {
                        JEditorPane editor = new JEditorPane(model.getFileReference().toURI().toURL());
                        editor.setEditable(false);
                        return editor;
                }
        }

        private JPanel getParserPanel(AbstractLogParser p, File file, long max) throws FileNotFoundException {
                logReader = null;
                JPanel panel = new JPanel(new SpringLayout());
                JButton button = new JButton("Parse file for analysis (this may take some time)");
                panel.add(new JLabel("...file is too big to display - analysis is possible but might run into performance issues"));
                panel.add(bar);
                panel.add(button);
                MonitoredInputStream mis = new MonitoredInputStream(new FileInputStream(file), max, 1024 * 1024 * 5);
                mis.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                bar.setValue(mis.getProgress());
                        }
                });
                ExecutorService backgroundExec = Executors.newCachedThreadPool();
                button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                button.setEnabled(false);
                                backgroundExec.execute(new Runnable() {

                                        @Override
                                        public void run() {
                                                try {
                                                        p.parse(mis, ParsingMode.COMPLETE);
                                                } catch (ParameterException | ParserException e) {
                                                        throw new RuntimeException(e);
                                                } finally {
                                                        LogGUIThreading.instance().execute(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                        bar.setValue(100);
                                                                        logReader = new LogParserAdapter(p);
                                                                        getModel().setLogReader(logReader);
                                                                        button.setText("File parsed");
                                                                }
                                                        });
                                                }
                                        }
                                });
                        }
                });
                SpringUtilities.makeCompactGrid(panel, 3, 1, 0, 200, 0, 0);

//		logReader=new LogParserAdapter(p);
                return panel;
        }

        public JButton getSciffButton() throws ParameterException, PropertyException, IOException {
                if (analyzeButton == null) {
                        analyzeButton = new JButton("Analyze with SCIFF");
                        ImageIcon icon;
                        icon = IconFactory.getIcon("search");
                        analyzeButton.setIcon(icon);

                        analyzeButton.addActionListener(new SciffAnalyzeAction(model.getFileReference()));
                }
                return analyzeButton;
        }

        @Override
        public JComponent getPropertiesView() {
                if (properties == null) {
                        try {
                                createPropertiesView();
                        } catch (IOException | ParameterException | PropertyException ex) {
                                Workbench.errorMessage("Could not get properties", ex, true);
                        }
                }
                return properties;
        }

        @Override
        public String getName() {
                //Wegen Nimbus LookaAndFeel
                if (model == null) {
                        return "null";
                }
                return model.getName();
        }

        @Override
        public String toString() {
                return model.getName();
        }

        public File getFile() {
                return model.getFileReference();
        }

        private void createPropertiesView() throws IOException, ParameterException, PropertyException {
                properties = new JPanel();
                properties.setLayout(new FlowLayout());
                if (model.getFileReference().getName().toLowerCase().endsWith(".mxml")) {
                        properties.add(new JLabel(".mxml file"));
                } else if (model.getFileReference().getName().toLowerCase().endsWith(".xes")) {
                        properties.add(new JLabel(".xes file"));
                } else {
                        properties.add(new JLabel("Textual file"));
                }
                properties.add(new JLabel("Size: " + model.getFileReference().length() / 1024 + "kB"));
                if (model.getFileReference().length() <= SWAT_FILE_TOO_BIG_TO_SHOW_SIZE) {
                        properties.add(new JLabel("Lines: " + FileUtils.getLineCount(model.getFileReference().getAbsolutePath(), Charset.defaultCharset().toString())));
                }
                //properties.add(getSciffButton());
                properties.validate();
                properties.repaint();
        }

        @Override
        public int hashCode() {
                return model.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                final LogFileViewer other = (LogFileViewer) obj;
                return Objects.equals(this.model, other.model);
        }

        public ISciffLogReader loadLogReader() throws FileNotFoundException, ParseException, IOException, ParameterException, ParserException {
                //remove MainComponent, add ProgressBar
                getViewport().removeAll();
                getViewport().add(bar);
                bar.setVisible(true);
                getViewport().revalidate();
                getViewport().repaint();
                revalidate();
                repaint();
                if (logReader == null) {
                        MonitoredInputStream mis = getInputStream();

                        if (!getModel().hasLogReaderSet()) {
                                switch (getModel().getType()) {
                                        case Aristaflow:
                                                logReader = new AristaFlowParser(getFileReference());
                                                ((AristaFlowParser) logReader).parse(whichTimestamp.BOTH);
                                                break;
                                        case MXML:
                                                MXMLLogParser mxmlParser = new MXMLLogParser();
                                                mxmlParser.parse(mis, ParsingMode.COMPLETE);
                                                logReader = new LogParserAdapter(mxmlParser);
                                                break;
                                        case XES:
                                                XESLogParser xesParser = new XESLogParser();
                                                xesParser.parse(getFileReference(), ParsingMode.COMPLETE);
                                                logReader = new LogParserAdapter(xesParser);
                                                break;
                                        default:
                                                break;
                                }
                                if (logReader != null) {
                                        getModel().setLogReader(logReader);
                                }

                        }
                }

                //remove progress bar, add main Component
                getViewport().removeAll();
                getViewport().add(mainComponent);
                getViewport().revalidate();
                getViewport().repaint();
                revalidate();
                repaint();

                return getModel().getLogReader();
        }

        public File getFileReference() {
                return getModel().getFileReference();
        }

        private MonitoredInputStream getInputStream() throws FileNotFoundException {
                MonitoredInputStream mis = new MonitoredInputStream(new FileInputStream(getFileReference()), getFileReference().length(), 1024 * 1024 * 5);
                mis.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {

                                SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                                System.out.println("new progress " + mis.getProgress());
                                                bar.setVisible(true);
                                                bar.setValue(mis.getProgress());

                                        }
                                });
                        }
                });
                return mis;
        }
}
