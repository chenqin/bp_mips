import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Help is a frame that is displayed whenever the user clicks the Help button.
 */
class Help extends Frame implements ActionListener
{
	/**
	 * the text area where the help information is displayed.
	 */
	public TextArea theText = new TextArea();
	/**
	 * button used to close the help frame.
	 */
	public Button OK = new Button("OK");
	/**
	 * GUI object that instantiated Help.
	 * @see GUI
	 */
	public GUI dad;
	
	/**
	 * performs the layout of the Help frame.
	 */
	Help(GUI parent)
	{
		dad = parent;
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(gridbag);

		dad.buildConstraints(constraints, 0, 0, 1, 1, 100, 90);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(theText, constraints);
		add(theText);
		
		dad.buildConstraints(constraints, 0, 1, 1, 1, 100, 10);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(OK, constraints);
		OK.addActionListener(this);
		add(OK);
		
		theText.insert("HELP\n"+
					"----\n\n"+
					"How to use the MIPS 5 Stage Pipeline Simulator.\n\n"+
					"Instructions supported:\n"+
					"------------ ----------\n\n"+
					"R-Type:\n"+
					"ADD,SUB,MULT,DIV,AND,OR,XOR,SLL,SRL,SLT,SLE,SGT,SGE,SEQ\n\n"+
					"I-Type:\n"+
					"ADDI,ANDI,ORI,XORI,SUBI,SLLI,SRLI\n\n"+
					"Memory Operations(I-Type):\n"+
					"LW,SW\n\n"+
					"Branching (I-Type):\n"+
					"BNE,BEQ\n\n"+
					"Format for R-Type instructions:\n"+
					"<op> $<dest> $<source1> $<source2>\n"+
					"ADD $3 $2 $1\n\n"+
					"Format for I-Type instructions:\n"+
					"<op> $<dest> $<source> <immediate>\n"+
					"ADDI $1 $1 100\n\n"+
					"Format for Memory Instructions:\n"+
					"<op> $<data reg> <immediate>($<offset reg>)\n"+
					"LW $1 100($2)\n\n"+
					"Format for Branching instructions:\n"+
					"<op> $<source1> $<source2> immediate\n"+
					"BNE $2 $1 100\n\n"+
					"There are no labels for branching; instead you must use the\n"+
					"line number of the instruction you wish to branch to as the\n"+
					"label.", 0
					);
		
		this.pack();
		setSize(new Dimension(500,300));
	}
	
	/**
	 * handles action events from the OK button.
	 */
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == OK)
		{
			this.setVisible(false);
		}
	}
}
