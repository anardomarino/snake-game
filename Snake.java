import javax.swing.JFrame;

public class Snake extends JFrame{
    public Snake(){
        add(new Game());
        setTitle("Anthony is a snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setSize(1000, 825);
        setLocation(170, 20);
    }
    public static void main(String[] args){
        new Snake();
    }
}