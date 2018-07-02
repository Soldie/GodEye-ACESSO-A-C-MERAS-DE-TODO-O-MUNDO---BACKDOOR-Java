package dev.adrielgro;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import de.javasoft.synthetica.plain.SyntheticaPlainLookAndFeel;
import com.fooock.shodan.*;

public class GodEye extends Surveillance {
    //private JList list1;
    private JPanel panelMain;
    private JToolBar toolbar1;
    private JButton surveillanceButton;
    private JButton settingsButton;
    private JButton logsButton;
    private JButton aboutButton;

    private JTable table1;
    private JButton scanButton;
    private JButton agregarPersonalizadoButton;
    private DefaultTableModel model;

    public GodEye() {
        try
        {
            UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Frame principal
        JFrame frame = new JFrame("GodEye - 1.0 [BETA]");
        //frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("godeye.png")));

        model = new DefaultTableModel(new String[] {"", "IP Address", "Port", "Type"}, 0){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false; //To change body of generated methods, choose Tools | Templates.
            }
        };
        table1.setModel(model);
        table1.getColumnModel().getColumn(0).setMinWidth(50);
        table1.getColumnModel().getColumn(0).setMaxWidth(50);
        table1.getColumnModel().getColumn(0).setPreferredWidth(50);
        table1.setRowSelectionAllowed(true);



        frame.setContentPane(this.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated (true);
        frame.setPreferredSize(new Dimension(800, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();


                for (String[] row : shodanSearch()) {
                    //System.out.println(ip.toString());
                    model.addRow(new Object[]{row[0], row[1], row[2], "RTSP"});
                }
                //shodanSearch();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table =(JTable) e.getSource();

                //table.clearSelection();
                //table1.setRowSelectionInterval(0, 3);

                //Point point = e.getPoint();
                //int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {

                    String currentIP = table1.getModel().getValueAt(table1.getSelectedRow(), 1).toString();
                    //JOptionPane.showMessageDialog(null, "Doble click: " + currentIP);

                    Runnable r = new VideoThread(currentIP); // Instanciamos la clase con el constructor
                    Thread t = new Thread(r); // Instanciamos un nuevo hilo con la clase anterior
                    t.start(); // Iniciamos el hilo
                    return;
                }
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table =(JTable) e.getSource();

                table1.getSelectionModel().setSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
                table1.getColumnModel().getSelectionModel().setSelectionInterval(0, 3);
            }
        });
        agregarPersonalizadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputIP = JOptionPane.showInputDialog(null, "IP Address:",
                        "Input Dialog", JOptionPane.PLAIN_MESSAGE);

                Runnable r = new VideoThread(inputIP); // Instanciamos la clase con el constructor
                Thread t = new Thread(r); // Instanciamos un nuevo hilo con la clase anterior
                t.start(); // Iniciamos el hilo
            }
        });
    }



}
