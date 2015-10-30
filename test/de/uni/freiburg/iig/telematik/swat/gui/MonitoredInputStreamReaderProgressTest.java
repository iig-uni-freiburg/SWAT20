package de.uni.freiburg.iig.telematik.swat.gui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.invation.code.toval.file.MonitoredInputStream;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;

public class MonitoredInputStreamReaderProgressTest {

        private static JProgressBar bar = new JProgressBar();

        public static void main(String[] args) throws ParameterException, ParserException, IOException {

                MXMLLogParser p = new MXMLLogParser();
                LogParser parser = new LogParser();
                List<LogTrace<LogEntry>> log = parser.parse(new File("/tmp/bla.xml")).get(0);
//        File file = new File("/tmp/log.mxml");
                File file = new File("/home/alange/B1large.mxml");
//        File file = new File("/home/alange/WriterTest.mxml");
//        File file = new File("/home/alange/validLogExample.mxml");
                long max = file.length();
                JFrame frame = new JFrame("title");
                frame.add(bar);
                frame.setSize(200, 100);
                frame.setVisible(true);

                try (
                        MonitoredInputStream mis = new MonitoredInputStream(new FileInputStream(file), max, 1024 * 1024 * 5)) {
                        BufferedInputStream in = new BufferedInputStream(new ProgressMonitorInputStream(null, "Reading " + file.toString(), new FileInputStream(file)));

                        mis.addChangeListener(new ChangeListener() {
                                @Override
                                public void stateChanged(ChangeEvent e) {
                                        bar.setValue(mis.getProgress());
                                        //frame.repaint();
                                        System.out.println(mis.getProgress());
                                }
                        });

                        p.parse(in, ParsingMode.COMPLETE);
                        frame.setVisible(false);
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }

                System.out.println(p.getSummary(0).getAverageTraceLength());
                
                System.exit(0);
        }
}
