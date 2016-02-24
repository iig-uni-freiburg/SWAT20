package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.file.MonitoredInputStream;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.format.LogFormatFactory;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.sewol.parser.AbstractLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.sewol.writer.LogWriter;
import de.uni.freiburg.iig.telematik.sewol.writer.PerspectiveException;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import static de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer.SWAT_FILE_TOO_BIG_TO_SHOW_SIZE;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

public class LogViewViewer extends JScrollPane implements ViewComponent {

        private final LogModel model;
        private final LogView view;

        private final String name;

        private JComponent mainComponent = null;
        private JComponent properties = null;

        private JButton exportButton = null;

        private final JProgressBar bar = new JProgressBar();

        private AbstractLogParser p;
        private ISciffLogReader logFileReader = null;
        private ISciffLogReader logViewReader = null;

        public LogViewViewer(LogView view) throws ProjectComponentException {
                this.view = view;
                model = SwatComponents.getInstance().getLog(view.getParentLogName());
                name = view.getName();
        }

        @Override
        public JComponent getMainComponent() {
                try {
                        mainComponent = getEditorField();
                        getViewport().add(mainComponent);
                        mainComponent.revalidate();
                        mainComponent.repaint();
                } catch (MalformedURLException e) {
                        Workbench.errorMessage("Could not generate Log viewer, URL malformed", e, true);
                } catch (IOException e) {
                        Workbench.errorMessage("Could not generate Log viewer, I/O Error", e, true);
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
                        return getParserPanel(p, model.getFileReference(), model.getFileReference().length());
                } else {
                        JEditorPane editor = new JEditorPane(model.getFileReference().toURI().toURL());
                        editor.setEditable(false);
                        return editor;
                }
        }

        private JPanel getParserPanel(AbstractLogParser p, File file, long max) throws FileNotFoundException {
                logFileReader = null;
                JPanel panel = new JPanel(new SpringLayout());
                JButton button = new JButton("Parse log file for analysis (this may take some time)");
                panel.add(new JLabel("...log file is too big to display - analysis is possible but might run into performance issues"));
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
                                                                        logFileReader = new LogParserAdapter(p);
                                                                        getModel().setLogReader(logFileReader);

                                                                        view.reinitialize();
                                                                        view.addTraces(p.getFirstParsedLog());
                                                                        logViewReader = new LogParserAdapter(view);

                                                                        exportButton.setEnabled(true);
                                                                        button.setText("File parsed");
                                                                }
                                                        });
                                                }
                                        }
                                });
                        }
                });
                SpringUtilities.makeCompactGrid(panel, 3, 1, 0, 200, 0, 0);

                return panel;
        }

        @Override
        public JComponent getPropertiesView() {
                if (properties == null) {
                        properties = new JPanel();
                        properties.setLayout(new FlowLayout());
                        properties.add(new JLabel(".view file"));
                        properties.add(new JLabel("Size: " + view.getFileReference().length() / 1024 + "kB"));
                        try {
                                properties.add(getExportButton());
                        } catch (ParameterException | PropertyException | IOException ex) {
                                throw new RuntimeException(ex);
                        }
                        properties.validate();
                        properties.repaint();
                }
                return properties;
        }

        @Override
        public String getName() {
                return name;
        }

        public LogModel getModel() {
                return model;
        }

        public LogView getView() {
                return view;
        }

        @Override
        public String toString() {
                return name;
        }

        @Override
        public int hashCode() {
                int hash = 3;
                hash = 89 * hash + Objects.hashCode(model);
                hash = 89 * hash + Objects.hashCode(view);
                return hash;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                final LogViewViewer other = (LogViewViewer) obj;
                if (!Objects.equals(this.model, other.model)) {
                        return false;
                }
                return Objects.equals(this.view, other.view);
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
                if (logFileReader == null || logViewReader == null) {
                        MonitoredInputStream mis = getInputStream();

                        if (!getModel().hasLogReaderSet()) {
                                AbstractLogParser parser = null;
                                switch (getModel().getType()) {
                                        case Aristaflow:
                                                logFileReader = new AristaFlowParser(model.getFileReference());
                                                ((AristaFlowParser) logFileReader).parse(AristaFlowParser.whichTimestamp.BOTH);
                                                // TODO initialize parser
                                                break;
                                        case MXML:
                                                parser = new MXMLLogParser();
                                                parser.parse(mis, ParsingMode.COMPLETE);
                                                logFileReader = new LogParserAdapter(parser);
                                                break;
                                        case XES:
                                                parser = new XESLogParser();
                                                parser.parse(model.getFileReference(), ParsingMode.COMPLETE);
                                                logFileReader = new LogParserAdapter(parser);
                                                break;
                                        default:
                                                break;
                                }
                                if (logFileReader != null && parser != null) {
                                        getModel().setLogReader(logFileReader);

                                        view.reinitialize();
                                        view.addTraces(parser.getFirstParsedLog());
                                        logViewReader = new LogParserAdapter(view);
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

                return logViewReader;
        }

        private JButton getExportButton() throws ParameterException, PropertyException, IOException {
                if (exportButton == null) {
                        exportButton = new JButton("Save as new log");
                        ImageIcon icon;
                        icon = IconFactory.getIcon("save");
                        exportButton.setIcon(icon);

                        exportButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                        // if not parsed yet => parse
                                        if (logViewReader == null) {
                                                MonitoredInputStream mis = null;
                                                AbstractLogParser parser = null;
                                                try {
                                                        mis = getInputStream();
                                                        switch (getModel().getType()) {
                                                                case Aristaflow:
                                                                        logFileReader = new AristaFlowParser(model.getFileReference());
                                                                        ((AristaFlowParser) logFileReader).parse(AristaFlowParser.whichTimestamp.BOTH);
                                                                        // TODO initialize parser
                                                                        break;
                                                                case MXML:
                                                                        parser = new MXMLLogParser();
                                                                        parser.parse(mis, ParsingMode.COMPLETE);
                                                                        logFileReader = new LogParserAdapter(parser);
                                                                        break;
                                                                case XES:
                                                                        parser = new XESLogParser();
                                                                        parser.parse(model.getFileReference(), ParsingMode.COMPLETE);
                                                                        logFileReader = new LogParserAdapter(parser);
                                                                        break;
                                                                default:
                                                                        break;
                                                        }
                                                        getModel().setLogReader(logFileReader);
                                                        if (parser != null) {
                                                                view.reinitialize();
                                                                view.addTraces(parser.getFirstParsedLog());
                                                                logViewReader = new LogParserAdapter(view);
                                                        }
                                                } catch (ParseException | ParameterException | ParserException | IOException ex) {
                                                        throw new RuntimeException(ex);
                                                } finally {
                                                        try {
                                                                if (mis != null) {
                                                                        mis.close();
                                                                }
                                                        } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                        }
                                                }
                                        }

                                        // call export action
                                        String newLogName = null;
                                        try {
                                                boolean firstRun = true;
                                                do {
                                                        if (!firstRun) {
                                                                JOptionPane.showMessageDialog(Workbench.getInstance(), "A log with the specified name \"" + newLogName + "\" already exists.", "Log already exists", JOptionPane.WARNING_MESSAGE);
                                                        }
                                                        newLogName = FileNameDialog.showDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), "Please specify a name for the new log", "Log Name", false);
                                                        firstRun = false;
                                                } while (newLogName != null && SwatComponents.getInstance().getLog(newLogName) != null);
                                        } catch (Exception ex) {
                                                throw new RuntimeException(ex);
                                        }
                                        if (newLogName != null) {
                                                String logDirectoryPath = model.getFileReference().getParentFile().getParentFile().toString() + File.separator;
                                                try {
                                                        final LogWriter w;
                                                        final File tempFile;
                                                        switch (model.getType()) {
                                                                case Aristaflow:
                                                                        // TODO add aristaflow support
                                                                        break;
                                                                case MXML:
                                                                        w = new LogWriter(LogFormatFactory.MXML(newLogName), logDirectoryPath, newLogName);
                                                                        for (Object traceObj : view.getTraces()) {
                                                                                LogTrace trace = (LogTrace) traceObj;
                                                                                w.writeTrace(trace);
                                                                        }
                                                                        w.setComment(createComment());
                                                                        w.closeFile();
                                                                        tempFile = new File(model.getFileReference().getParentFile().getParentFile(), newLogName + ".mxml");
                                                                        SwatComponents.getInstance().getContainerMXMLLogs().addComponent(tempFile);
                                                                        SwatComponents.getInstance().getContainerMXMLLogs().storeComponents();
                                                                        tempFile.delete();
                                                                        break;
                                                                case XES:
                                                                        w = new LogWriter(LogFormatFactory.XES(newLogName), logDirectoryPath, newLogName);
                                                                        for (Object traceObj : view.getTraces()) {
                                                                                LogTrace trace = (LogTrace) traceObj;
                                                                                w.writeTrace(trace);
                                                                        }
                                                                        w.setComment(createComment());
                                                                        w.closeFile();
                                                                        tempFile = new File(model.getFileReference().getParentFile().getParentFile(), newLogName + ".xes");
                                                                        SwatComponents.getInstance().getContainerXESLogs().addComponent(tempFile);
                                                                        SwatComponents.getInstance().getContainerXESLogs().storeComponents();
                                                                        tempFile.delete();
                                                                        break;
                                                        }
                                                } catch (IOException | PerspectiveException | ProjectComponentException ex) {
                                                        throw new RuntimeException(ex);
                                                }
                                        }
                                }
                        });
                }
                return exportButton;
        }

        private MonitoredInputStream getInputStream() throws FileNotFoundException {
                MonitoredInputStream mis = new MonitoredInputStream(new FileInputStream(model.getFileReference()), model.getFileReference().length(), 1024 * 1024 * 5);
                mis.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {

                                SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                                bar.setVisible(true);
                                                bar.setValue(mis.getProgress());
                                        }
                                });
                        }
                });
                return mis;
        }

        private String createComment() {
                StringBuilder sb = new StringBuilder();
                sb.append("Created from\n");
                sb.append(view.toString());
                return sb.toString();
        }
}
