import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import XWindow.XWindow;

public class App {
    static String selectedWaveType = "";
    static GraphPoint[] wavePoint = new GraphPoint[2];
    static boolean isRunning = true;
    static int holdFontSize = 40;
    static boolean holdFontIncreaseDirection = true;
    static int waveSpeed = 10;

    public static void main(String[] args) throws Exception {
        XWindow window = new XWindow(1000, 700, "Wave Simulator", true);
        window.setMinimizeEnable(true);

        wavePoint[0] = new GraphPoint(50, 100);
        wavePoint[1] = new GraphPoint(950, 100);

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int printSumGraph = 0; printSumGraph < 1000; printSumGraph++) {
                    g.setColor(Color.green);
                    int sumGraphLevel = 0;
                    if (Math.abs(wavePoint[0].x - printSumGraph) < 100) {
                        sumGraphLevel += (int) (wavePoint[0].max
                                * Math.cos((printSumGraph - wavePoint[0].x) * Math.PI / 200.0) * -1);
                    }
                    if (Math.abs(wavePoint[1].x - printSumGraph) < 100) {
                        sumGraphLevel += (int) (wavePoint[1].max
                                * Math.cos((printSumGraph - wavePoint[1].x) * Math.PI / 200.0) * -1);
                    }
                    g.drawLine(printSumGraph, sumGraphLevel + 345, printSumGraph, sumGraphLevel + 355);
                }

                for (int printGraph1 = -100; printGraph1 <= 100; printGraph1++) {
                    g.setColor(Color.blue);
                    int graph1Level = (int) (wavePoint[0].max * Math.cos((printGraph1) * Math.PI / 200.0) * -1);

                    g.drawLine(wavePoint[0].x + printGraph1, graph1Level + 345, wavePoint[0].x + printGraph1,
                            graph1Level + 355);
                }
                for (int printGraph2 = -100; printGraph2 <= 100; printGraph2++) {
                    g.setColor(Color.orange);
                    int graph2Level = (int) (wavePoint[1].max * Math.cos((printGraph2) * Math.PI / 200.0) * -1);

                    g.drawLine(wavePoint[1].x + printGraph2, graph2Level + 345, wavePoint[1].x + printGraph2,
                            graph2Level + 355);
                }
                g.setColor(Color.white);
                for (int i = 0; i < 1000; i += 1) {
                    g.drawLine(i, 345, i, 355);
                }

                if (isRunning) {
                    wavePoint[0].x++;
                    wavePoint[1].x--;
                } else {
                    g.setFont(new Font("", 0, holdFontSize));
                    g.setColor(Color.red);
                    g.drawString("HOLD", 0, holdFontSize);
                    if (holdFontIncreaseDirection) {
                        holdFontSize++;
                    } else {
                        holdFontSize--;
                    }
                    if (holdFontSize > 40) {
                        holdFontIncreaseDirection = false;
                    } else if (holdFontSize < 30) {
                        holdFontIncreaseDirection = true;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (wavePoint[0].x > 1000) {
                    wavePoint[0].x = 0;
                    wavePoint[1].x = 950;
                }
                try {
                    Thread.sleep(waveSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        };
        graphPanel.setBackground(new Color(64, 64, 64));
        JPanel controlPanel = new JPanel();

        window.add(graphPanel);
        window.add(controlPanel, "North");

        JComboBox<String> waveTypeSelectBox = new JComboBox<String>();
        String[] waveTypes = { "constructive", "destructive" };
        JComboBox<String> waveSpeedSelectBox = new JComboBox<String>();
        String[] waveSpeeds = { "slow", "normal", "fast" };

        for (String waveType : waveTypes) {
            waveTypeSelectBox.addItem(waveType);
        }
        controlPanel.add(waveTypeSelectBox);

        for (String waveSpeed : waveSpeeds) {
            waveSpeedSelectBox.addItem(waveSpeed);
        }
        controlPanel.add(waveSpeedSelectBox);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.getSource() == graphPanel) {
                    isRunning = !isRunning;
                }
            }
        };

        graphPanel.addMouseListener(mouseAdapter);

        window.setVisible(true);

        Thread optionChange = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (selectedWaveType != waveTypeSelectBox.getSelectedItem()) {
                        selectedWaveType = waveTypes[waveTypeSelectBox.getSelectedIndex()];
                        System.out.println(selectedWaveType);

                        if (selectedWaveType == "constructive") {
                            wavePoint = new GraphPoint[2];
                            wavePoint[0] = new GraphPoint(50, 100);
                            wavePoint[1] = new GraphPoint(graphPanel.getWidth()-50, 50);
                        } else if (selectedWaveType == "destructive") {
                            wavePoint = new GraphPoint[2];
                            wavePoint[0] = new GraphPoint(50, 100);
                            wavePoint[1] = new GraphPoint(graphPanel.getWidth()-50, -100);
                        }
                    }
                }
            }
        };
        Thread speedChange = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (waveSpeedSelectBox.getSelectedItem() == "slow") {
                        waveSpeed = 20;
                    } else if (waveSpeedSelectBox.getSelectedItem() == "normal") {
                        waveSpeed = 10;
                    } else if (waveSpeedSelectBox.getSelectedItem() == "fast") {
                        waveSpeed = 5;
                    }
                }
            }
        };
        optionChange.start();
        speedChange.start();
    }
}

class GraphPoint {
    public int x;
    public int max;

    public GraphPoint(int x, int max) {
        this.x = x;
        this.max = max;
    }
}