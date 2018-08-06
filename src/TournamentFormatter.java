import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.MalformedURLException;
import java.util.HashSet;

public class TournamentFormatter extends JFrame{

    private JTextArea taOutput;
    private JButton btnAdd;
    private JComboBox cbbTourneys;

    private static HashSet<Team> teams = new HashSet<>();
    private Session s = new Session();
    private JTextField tfURL;
    private JTextField tfEventID;
    private JButton btnDelete;
    private JPanel panelMain;
    private JPanel panelImport;
    private JButton btnClipboard;
    private JButton btnEdit;
    private JComboBox cbbTeams;
    private JLabel teamlbl;
    private JComboBox cbbStatus;
    private JLabel statuslbl;
    private JTextField tfNotes;
    private JLabel notelbl;
    private JButton btnNotes;

    private String importString;
    private static int FRAMESIZE = 5;
    private static int TOURNAMENTSIZE = 4;
    private static int NAMEOFFSET = 4;
    private static int URLOFFSET = 1;


    public TournamentFormatter(){
        initUI();
    }

    private void createContentUI(){
        setContentPane(panelMain);

        for (Team t : teams) {
            cbbTeams.addItem(t);
        }

        for (int i = 0; i < DscString.placementsKeys.length; i++)
        {
            cbbStatus.addItem(DscString.placementsKeys[i]);
        }

        btnAdd.addActionListener(evt -> {
            Tournament tnmt = null;
            try {
                tnmt = new Tournament(tfURL.getText(), tfEventID.getText(), (String)cbbStatus.getSelectedItem());
                s.addTournament(tnmt);
                s.setTeam((Team) cbbTeams.getSelectedItem());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            tfURL.setText("");
            tfEventID.setText("");
            taOutput.setText(s.toString());
            cbbTourneys.addItem(tnmt.getName());
        });

        btnClipboard.addActionListener(e -> {
            StringSelection selection = new StringSelection(taOutput.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        });

        btnEdit.addActionListener(e -> {
            for(int i = 0; i < s.tournaments.size(); i++){
                if(s.tournaments.get(i).getName().equals(cbbTourneys.getSelectedItem())){
                    tfURL.setText(s.tournaments.get(i).getBracketURL());
                    tfEventID.setText(s.tournaments.get(i).getEventID());
                    cbbTourneys.removeItem(s.tournaments.get(i).getName());
                    cbbStatus.setSelectedItem(s.tournaments.get(i).getStatus());
                    s.tournaments.remove(s.tournaments.get(i));
                }
            }

        });
        btnDelete.addActionListener(e -> {
            if(s.tournaments != null){
                for(int i = 0; i < s.tournaments.size(); i++){
                    if(s.tournaments.get(i).getName().equals(cbbTourneys.getSelectedItem())){
                        cbbTourneys.removeItem(s.tournaments.get(i).getName());
                        s.tournaments.remove(s.tournaments.get(i));
                        taOutput.setText(s.toString());
                    }
                }
            }
        });

        btnNotes.addActionListener(e -> {
            if(!tfNotes.getText().isEmpty()) {
                s.addNotes(tfNotes.getText());
                taOutput.setText(s.toString());
            }
        });
        pack();
    }

    private void initUI(){
        createMenuBar();
        createContentUI();

        setTitle("Tournament Formatter");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createMenuBar(){
        JMenuBar menubar = new JMenuBar();
        //ImageIcon exitIcon = new ImageIcon("src/main/resources/exit.png");

        JMenu file = new JMenu("File");

        final SpringLayout layout = new SpringLayout();

        panelImport = new JPanel(layout);
        panelImport.setPreferredSize(new Dimension(310, 100));

        JTextArea taImport = new JTextArea();
        taImport.setBorder(BorderFactory.createLineBorder(Color.black));
        taImport.setLineWrap(true);
        taImport.setWrapStyleWord(true);
        taImport.setPreferredSize(new Dimension(300,100));
        panelImport.add(taImport);

        JMenuItem importME = new JMenuItem("Import");
        importME.setToolTipText("Exit application");
        importME.addActionListener((ActionEvent event) -> {
            int result = JOptionPane.showConfirmDialog(this, panelImport,
                    "Import", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                s = importSession(taImport.getText());
                updateUI();
            }
        });

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        file.add(importME);
        file.add(eMenuItem);

        menubar.add(file);

        setJMenuBar(menubar);
    }

    private void updateUI(){
        taOutput.setText(s.toString());
        cbbTeams.setSelectedItem(s.getTeam());
        tfURL.setText("");
        tfEventID.setText("");
        cbbTourneys.removeAll();
        for(int i = 0; i < s.tournaments.size(); i++){
            cbbTourneys.addItem(s.tournaments.get(i).getName());
        }
        cbbStatus.setSelectedIndex(0);
    }

    private static Session importSession(String sImport){
        Team tImport = null;
        Session sessionImport = new Session();

        String[] sImportRows = sImport.split("\n");

        for(int i = 0; i < (sImportRows.length - FRAMESIZE) / TOURNAMENTSIZE; i++){
            try {
                String bracketURL = DscString.unwrap(sImportRows[i * TOURNAMENTSIZE + 2].split(" ")[1],"<");
                String eventURL = DscString.unwrap(sImportRows[i * TOURNAMENTSIZE + 3].split(" ")[1], "<");
                String eventID = eventURL.split("/")[eventURL.split("/").length - 1];
                String status = DscString.reverseplacements.get(sImportRows[i * TOURNAMENTSIZE + 1].split("\\*\\*__: ")[1]);
                String notes = DscString.unwrap(sImportRows[sImportRows.length - 1], "**").substring(6);

                Tournament t = new Tournament(bracketURL, eventID, status);
                sessionImport.addTournament(t);
                sessionImport.addNotes(notes);
            } catch (MalformedURLException e) {
                System.err.println(e);
            }
        }

        for (Team t : teams){
            if(t.toString().equals(sImportRows[sImportRows.length - 3])) tImport = t;
        }

        if(tImport == null) System.err.println("Team not found");
        else sessionImport.setTeam(tImport);

        return sessionImport;
    }

    public static void main(String[] args) {
        loadTeams();
        SwingUtilities.invokeLater(() -> {
            TournamentFormatter tg = new TournamentFormatter();
            tg.setVisible(true);
        });
    }

    public static void loadTeams() {
        teams.add(new Team("gglol"));
        teams.add(new Team("lolgg"));
        teams.add(new Team("High skilled aram tournament team haha"));
        teams.add(new Team("Everything Is Okay"));
    }

    public static void test2(){
        String testurl = "https://battlefy.com/gamecracy/gamecracy%E2%80%99s-aram-%5Ball-random%5D-j29-4p-%5Beuw%5D/5b523ad199fcd90398e4e30f/stage/5b5dd586899b2a03c1cc703f/bracket/?hidecomplete=true";
        String testID = "340634";
        HashSet<Team> teams = new HashSet<>();
        teams.add(new Team("High skilled aram tournament team haha"));
        teams.add(new Team("lolgg"));
        Session s = new Session();
        try {
            s.addTournamentFromURL(testurl, testID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(s.toString());
    }

}
