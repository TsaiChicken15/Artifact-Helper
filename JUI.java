import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JUI extends JFrame implements ActionListener{
    Character currentCharacter = Character.Mika;
	ArrayList<JButton> substats = new ArrayList<JButton>();
    ArrayList<JButton> weights = new ArrayList<JButton>();
	ArrayList<JTextField> values = new ArrayList<JTextField>();
    JComboBox<String> characterList;
    JButton result, update;
    int rating;

	JUI() {
		substats.clear();
        values.clear();
		int index = 0;
		for(Substat s: Substat.values()) {
			JButton item = new JButton();
            item.setBackground(Color.LIGHT_GRAY);
            item.setBorderPainted(false);
			item.setBounds(4, 4 + index * (16 + 8 + 4), 128 + 64, 16 + 8);
			item.setFocusable(false);
            item.setModel(new DefaultButtonModel() {
                @Override
                public boolean isPressed() {
                    return false;
                }
            });
			item.setText(s.getName());
            substats.add(item);
			this.add(item);
			index++;
		}
        index = 0;
        int valueIndex = 0;
        for(Substat s: Substat.values()) {
			JButton item = new JButton();
            item.setBorderPainted(false);
			item.setBounds(4 + 128 + 64 + 8, 4 + index * (16 + 8 + 4), 128, 16 + 8);
			item.setFocusable(false);
            item.setModel(new DefaultButtonModel() {
                @Override
                public boolean isPressed() {
                    return false;
                }
            });
            if ( s == Substat.SUBSTAT ) {
                item.setBackground(Color.LIGHT_GRAY);
                item.setName("權重/Weight");
                item.setText("權重/Weight");
                JButton item1 = new JButton();
                item1.setBorderPainted(false);
                item1.setBounds(4 + 256 + 64 + 16, 4, 128, 16 + 8);
                item1.setFocusable(false);
                item1.setModel(new DefaultButtonModel() {
                    @Override
                    public boolean isPressed() {
                        return false;
                    }
                });
                item1.setBackground(Color.LIGHT_GRAY);
                item1.setName("數值/Value");
                item1.setText("數值/Value");
                this.add(item1);
            } else {
                JTextField field = new JTextField("0", 4);
			    field.setBounds(4 + 256 + 64 + 16, 4 + index * (16 + 8 + 4), 128, 16 + 8);
			    field.setFocusable(true);
                values.add(field);
                this.add(field, BorderLayout.SOUTH);
                item.setBackground(Color.WHITE);
			    item.setText(String.valueOf(currentCharacter.getWeight(valueIndex - 1) / 100));
                weights.add(item);
                valueIndex++;
            }
			this.add(item);
			index++;
		}

        characterList = new JComboBox<String>(Character.getNames());
        characterList.addActionListener(e -> comboBoxIsChanged(e));
        characterList.setBackground(Color.LIGHT_GRAY);
        characterList.setBounds(512 - 32 - 4, 4, 128 + 64, 16 + 8);
        characterList.setFocusable(false);
        characterList.setSelectedItem(Character.Mika.getName());
        this.add(characterList); 
		
        update = new JButton();
        update.addActionListener(e -> update(e));
        update.setBackground(Color.LIGHT_GRAY);
        update.setBorderPainted(false);
        update.setBounds(512 - 32 - 4, 4 + 8 * (16 + 8 + 4), 128 + 64, 16 + 8);
        update.setFocusable(false);
        update.setModel(new DefaultButtonModel() {
            @Override
            public boolean isPressed() {
                return false;
            }
        });
        update.setText("更新/Update");
        this.add(update);

        result = new JButton();
        result.setBackground(Color.LIGHT_GRAY);
        result.setBorderPainted(false);
        result.setBounds(512 - 32 - 4, 4 + 9 * (16 + 8 + 4), 128 + 64, 32 + 16 + 2);
        result.setFocusable(false);
        result.setModel(new DefaultButtonModel() {
            @Override
            public boolean isPressed() {
                return false;
            }
        });
        result.setText(String.valueOf(rating));
        this.add(result);

		this.setTitle("聖遺物評分計算機/Artifact Helper");
		this.setIconImage(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(null);
		this.setSize(640 + 32 + 16, 320 + 32);
		this.setVisible(true);
		
	}
	
    void comboBoxIsChanged(ActionEvent e) {
        currentCharacter = ((Character)getCharacter((String)characterList.getSelectedItem()));
        int index = 0;
		for(JButton item: weights) {
            if ( item.getName() == "權重/Weight" ) continue;
			item.setText(String.valueOf(((Character)getCharacter((String)characterList.getSelectedItem())).getWeight(index)));
            index++;
        }
	}

    void update(ActionEvent e) {
        rating = 0;
        int index = 0;
        for(Substat item: Substat.values()) { // Integer.parseInt(values.get(index).getText())
            if ( item.getName() == "副詞條/Substat" ) continue;
            rating += CalculateRating(Double.parseDouble(values.get(index).getText()), item, currentCharacter.getWeight(index));
            System.out.println(item.getName() + ": " + String.format("%.2f", Double.parseDouble(values.get(index).getText())) +
                " \t倍率/Multiplier: " + String.format("%.5f", item.getMultiplier()) + 
                " \t權重/Weight: " + (double)(currentCharacter.getWeight(index)) / 100 + 
                " \t分數/Rating: " + CalculateRating(Double.parseDouble(values.get(index).getText()), item, currentCharacter.getWeight(index)));
            index++;
        }
        result.setText(String.valueOf(Math.round(rating)));
    }

	public double CalculateRating(double VALUE, Substat SUBSTAT, double WEIGHT) {
		return Math.round(VALUE * SUBSTAT.getMultiplier() * WEIGHT / 100);
	}

	public enum Substat {
        SUBSTAT("副詞條/Substat", 0.0D),
		CRIT_RATE("爆擊率/CR", 2.0D),
		CRIT_DMG("爆擊傷害/CD", 1.0D),
		EM("元素精通/EM", 0.33D),
		ER("充能效率/ER", 1.1979D),
		ATKP("攻擊力%/ATK%", 1.33D),
		HPP("生命值%/HP%", 1.33D),
		DEFP("防禦力%/DEF%", 1.06D),
		ATK("攻擊力/ATK", 0.398D * 0.5D),
		HP("生命值/HP", 0.026D * 0.66D),
		DEF("防禦力/DEF", 0.335D * 0.66D);
		
		public String name;
		public double multiplier;
		
		private Substat(String name, double multiplier) {
			this.name = name;
        	this.multiplier = multiplier;
        }
		
		public String getName() {
			return this.name;
		}
		
		public double getMultiplier() {
			return this.multiplier;
		}
	}

    public enum Character {
        Mika("米卡/Mika", 100, 50, 0, 50, 50, 0, 90),
        Dehya("迪希雅/Dehya", 75, 75, 0, 100, 100, 0, 0),
        Alhaithm("艾爾海森/Alhaithm", 0, 75, 0, 100, 100, 75, 0),
        Yaoyao("遙遙/Yaoyao", 100, 50, 0, 50, 50, 0, 75),
        Wanderer("流浪者/Wanderer", 0, 75, 0, 100, 100, 0, 0),
        Faruzan("法璐珊/Faruzan", 0, 55, 0, 100, 100, 0, 75),
        Nahida("納西妲/Nahida", 0, 55, 0, 100, 100, 100, 0),
        Layla("萊伊菈/Layla", 100, 55, 0, 100, 100, 0, 55),
        Nilou("尼露/Nilou", 100, 0, 0, 100, 100, 75, 0),    
        Cyno("賽諾/Cyno", 0, 75, 0, 100, 100, 75, 0),      
        Candace("坎蒂斯/Candace", 100, 0, 0, 50, 50, 0, 55),
        Dori("多利/Dori", 75, 75, 0, 50, 50, 0, 55),      
        Tighnari("提納里/Tighnari", 0, 75, 0, 100, 100, 75, 0),
        Collei("柯萊/Collei", 0, 75, 0, 100, 100, 75, 55),
        Heizou("鹿野原平藏/Heizou", 0, 75, 0, 100, 100, 75, 0),
        Shinobu("久崎忍/Shinobu", 100, 75, 0, 50, 50, 100, 55),
        Yelan("夜蘭/Yelan", 80, 0, 0, 100, 100, 0, 0),
        Ayato("神里陵人/Ayato", 55, 75, 0, 100, 100, 0, 0),
        Yae("八重神子/Yae", 0, 75, 0, 100, 100, 75, 0),
        Shenhe("申鶴/Shenhe", 0, 100, 0, 100, 100, 0, 55),
        Yunjin("雲菫/Yunjin", 0, 0, 100, 50, 50, 0, 90),
        Itto("荒瀧一斗/Itto", 0, 50, 100, 100, 100, 0, 30),
        Gorou("五郎/Gorou", 0, 50, 100, 50, 50, 0, 90),
        Bennett("班尼特/Bennett", 100, 50, 0, 50, 50, 0, 55),
        Kazuha("楓原萬葉/Kazuha", 0, 75, 0, 100, 100, 100, 55),
        Shougun("雷電將軍/Shougun", 0, 75, 0, 100, 100, 0, 75),
        Xingqiu("行秋/Xingqiu", 0, 75, 0, 100, 100, 0, 55),
        Zhongli("鍾離/Zhongli", 80, 75, 0, 100, 100, 0, 55),
        Ayaka("神里陵華/Ayaka", 0, 75, 0, 100, 100, 0, 0),
        Xiangling("香菱/Xiangling", 0, 75, 0, 100, 100, 75, 55),
        Hutao("胡桃/Hutao", 80, 50, 0, 100, 100, 75, 0),
        Ganyu("甘雨/Ganyu", 0, 75, 0, 100, 100, 75, 0),
        Venti("溫蒂/Venti", 0, 75, 0, 100, 100, 75, 55),
        Kokomi("珊湖宮心海/Kokomi", 100, 50, 0, 0, 0, 75, 55),
        Mona("莫那/Mona", 0, 75, 0, 100, 100, 75, 75),
        Albedo("阿貝多/Albedo", 0, 0, 100, 100, 100, 0, 0),
        Diona("迪奧那/Diona", 100, 50, 0, 50, 50, 0, 90),
        Eula("優菈/Eula", 0, 75, 0, 100, 100, 0, 30),
        Tartaglia("達達利亞/Tartaglia", 0, 75, 0, 100, 100, 75, 0),
        Xiao("魈/Xiao", 0, 75, 0, 100, 100, 0, 55),
        Yoimiya("霄宮/Yoimiya", 0, 75, 0, 100, 100, 75, 0),
        Sara("九條裟羅/Sara", 0, 75, 0, 100, 100, 0, 55),
        Jean("琴/Jean", 0, 75, 0, 100, 100, 0, 55),
        Fischl("菲謝爾/Fischl", 0, 75, 0, 100, 100, 75, 0),
        Rosaria("羅莎莉亞/Rosaria", 0, 75, 0, 100, 100, 0, 0),
        Klee("可莉/Klee", 0, 75, 0, 100, 100, 75, 0),
        Ningguang("凝光/Ningguang", 0, 75, 0, 100, 100, 0, 0),
        Beidou("北斗/Beidou", 0, 75, 0, 100, 100, 75, 55),
        Keqing("刻晴/Keqing", 0, 75, 0, 100, 100, 75, 0),
        Tohma("托馬/Tohma", 100, 50, 0, 50, 50, 0, 90),
        Diluc("迪盧克/Diluc", 0, 75, 0, 100, 100, 75, 0),
        Barbara("芭芭拉/Barbara", 50, 55, 0, 50, 50, 0, 55),
        Noel("諾艾爾/Noel", 0, 50, 90, 100, 100, 0, 70),
        TravelerDendro("旅行者水/TravelerDendro", 0, 75, 0, 100, 100, 75, 55),
        TravelerElectro("旅行者雷/TravelerElectro", 0, 75, 0, 100, 100, 75, 0),
        TravelerGeo("旅行者岩/TravelerGeo", 0, 75, 0, 100, 100, 0, 0),
        TravelerAnemo("旅行者風/TravelerAnemo", 0, 75, 0, 100, 100, 75, 0),
        Chongyun("重雲/Chongyun", 0, 75, 0, 100, 100, 75, 55),
        Qiqi("七七/Qiqi", 0, 100, 0, 100, 100, 0, 55),
        Kaeya("凱亞/Kaeya", 0, 75, 0, 100, 100, 0, 0),
        Yanfei("煙緋/Yanfei", 0, 75, 0, 100, 100, 75, 0),
        Sayu("早柚/Sayu", 0, 50, 0, 50, 50, 100, 55),
        Ambor("安柏/Ambor", 0, 75, 0, 100, 100, 75, 0),
        Lisa("麗莎/Lisa", 0, 75, 0, 100, 100, 75, 0),
        Aloy("阿洛伊/Aloy", 0, 75, 0, 100, 100, 0, 0),
        Xinyan("辛炎/Xinyan", 0, 75, 0, 100, 100, 0, 55),
        Sucrose("砂糖/Sucrose", 0, 75, 0, 100, 100, 100, 55),
        Razor("雷澤/Razor", 0, 75, 0, 100, 100, 0, 0);
		
		public String name;
		public int HP, ATK, DEF, CR, CD, EM, ER;
		
		private Character(String name, int HP, int ATK, int DEF, int CR, int CD, int EM, int ER) {
			this.name = name;
        	this.HP = HP;
            this.ATK = ATK;
            this.DEF = DEF;
            this.CR = CR;
            this.CD = CD;
            this.EM = EM;
            this.ER = ER;
        }
		
		public String getName() {
			return this.name;
		}

        public static String[] getNames() {
			ArrayList<String> list = new ArrayList<String>();
            for(Character c: Character.values()) {
                list.add(c.getName());
            }
            String name_list[] = list.toArray(new String[0]);
            return name_list;
		}
		
        public double getWeight(int i) {
            if ( i == 0 )
                return this.CR;
            else if ( i == 1 )
                return this.CD;
            else if ( i == 2 )
                return this.EM;
            else if ( i == 3 )
                return this.ER;
            else if ( i == 4 )
                return this.ATK;
            else if ( i == 5 )
                return this.HP;
            else if ( i == 6 )
                return this.DEF;
            else if ( i == 7 )
                return this.ATK;
            else if ( i == 8 )
                return this.HP;
            else if ( i == 9 )
                return this.DEF;
            return 0;
        }

		public int getHP() {
            return HP;
        }

        public int getATK() {
            return ATK;
        }

        public int getDEF() {
            return DEF;
        }

        public int getCR() {
            return CR;
        }

        public int getCD() {
            return CD;
        }

        public int getEM() {
            return EM;
        }

        public int getER() {
            return ER;
        }
	}

    public Character getCharacter(String name) {
        for(Character item: Character.values()) {
            if (name == item.getName()) {
                return item;
            }
        }
        return null;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}