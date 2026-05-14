public class Data {

    private Object data[][];
    private int numberOfExamples;
    private Attribute explanatorySet[];
    private ContinuousAttribute classAttribute;

    public Data() {

        // 1. Definiamo i valori discreti per ogni attributo
        String motorValues[] = { "A", "B", "C", "D", "E" };
        String screwValues[] = { "A", "B", "C", "D", "E" };
        String pgainValues[] = { "3", "4", "5", "6" };
        String vgainValues[] = { "1", "2", "3", "4", "5" };

        // 2. Creiamo l'array degli attributi indipendenti
        explanatorySet = new Attribute[4];
        explanatorySet[0] = new DiscreteAttribute("motor", 0, motorValues);
        explanatorySet[1] = new DiscreteAttribute("screw", 1, screwValues);
        explanatorySet[2] = new DiscreteAttribute("pgain", 2, pgainValues);
        explanatorySet[3] = new DiscreteAttribute("vgain", 3, vgainValues);

        // 3. Creiamo l'attributo di classe
        classAttribute = new ContinuousAttribute("class", 4);

        // 4. Numero di esempi
        numberOfExamples = 167;

        // 5. Creiamo la matrice dei dati
        data = new Object[numberOfExamples][5];
        data[0] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[1] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[2] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[3] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[4] = new Object[] { "D", "B", "6", "5", 0.356 };
        data[5] = new Object[] { "B", "D", "4", "3", 0.731 };
        data[6] = new Object[] { "D", "C", "6", "5", 0.356 };
        data[7] = new Object[] { "C", "C", "3", "2", 0.731 };
        data[8] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[9] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[10] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[11] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[12] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[13] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[14] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[15] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[16] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[17] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[18] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[19] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[20] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[21] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[22] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[23] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[24] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[25] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[26] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[27] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[28] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[29] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[30] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[31] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[32] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[33] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[34] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[35] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[36] = new Object[] { "D", "A", "4", "3", 0.481 };
        data[37] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[38] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[39] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[40] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[41] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[42] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[43] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[44] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[45] = new Object[] { "B", "E", "6", "5", 0.381 };
        data[46] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[47] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[48] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[49] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[50] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[51] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[52] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[53] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[54] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[55] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[56] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[57] = new Object[] { "B", "E", "3", "2", 0.831 };
        data[58] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[59] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[60] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[61] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[62] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[63] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[64] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[65] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[66] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[67] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[68] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[69] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[70] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[71] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[72] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[73] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[74] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[75] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[76] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[77] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[78] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[79] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[80] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[81] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[82] = new Object[] { "B", "E", "3", "2", 0.831 };
        data[83] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[84] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[85] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[86] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[87] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[88] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[89] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[90] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[91] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[92] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[93] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[94] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[95] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[96] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[97] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[98] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[99] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[100] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[101] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[102] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[103] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[104] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[105] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[106] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[107] = new Object[] { "B", "E", "3", "2", 0.831 };
        data[108] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[109] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[110] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[111] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[112] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[113] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[114] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[115] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[116] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[117] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[118] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[119] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[120] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[121] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[122] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[123] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[124] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[125] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[126] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[127] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[128] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[129] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[130] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[131] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[132] = new Object[] { "B", "E", "3", "2", 0.831 };
        data[133] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[134] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[135] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[136] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[137] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[138] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[139] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[140] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[141] = new Object[] { "D", "E", "4", "3", 0.356 };
        data[142] = new Object[] { "B", "A", "3", "2", 0.831 };
        data[143] = new Object[] { "E", "D", "5", "4", 0.281 };
        data[144] = new Object[] { "A", "D", "4", "3", 0.906 };
        data[145] = new Object[] { "B", "E", "6", "5", 0.506 };
        data[146] = new Object[] { "D", "B", "4", "3", 0.356 };
        data[147] = new Object[] { "B", "C", "3", "2", 0.731 };
        data[148] = new Object[] { "E", "E", "5", "4", 0.281 };
        data[149] = new Object[] { "A", "E", "4", "3", 0.906 };
        data[150] = new Object[] { "B", "A", "6", "5", 0.506 };
        data[151] = new Object[] { "D", "C", "4", "3", 0.356 };
        data[152] = new Object[] { "B", "D", "3", "2", 0.831 };
        data[153] = new Object[] { "E", "A", "5", "4", 0.281 };
        data[154] = new Object[] { "A", "A", "4", "3", 0.906 };
        data[155] = new Object[] { "B", "B", "6", "5", 0.506 };
        data[156] = new Object[] { "D", "D", "4", "3", 0.356 };
        data[157] = new Object[] { "B", "E", "3", "2", 0.831 };
        data[158] = new Object[] { "E", "B", "5", "4", 0.281 };
        data[159] = new Object[] { "A", "B", "4", "3", 0.906 };
        data[160] = new Object[] { "B", "C", "6", "5", 0.506 };
        data[161] = new Object[] { "D", "A", "4", "3", 0.356 };
        data[162] = new Object[] { "B", "B", "3", "2", 0.831 };
        data[163] = new Object[] { "E", "C", "5", "4", 0.281 };
        data[164] = new Object[] { "A", "C", "4", "3", 0.906 };
        data[165] = new Object[] { "B", "D", "6", "5", 0.506 };
        data[166] = new Object[] { "D", "E", "4", "3", 0.356 };
    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfExplanatoryAttributes() {
        return explanatorySet.length;
    }

    public Double getClassValue(int exampleIndex) {
        return (Double) data[exampleIndex][classAttribute.getIndex()];
    }

    public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
        return data[exampleIndex][attributeIndex];
    }

    public Attribute getExplanatoryAttribute(int index) {
        return explanatorySet[index];
    }

    public ContinuousAttribute getClassAttribute() {
        return classAttribute;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < numberOfExamples; i++) {
            for (int j = 0; j < explanatorySet.length; j++) {
                s += data[i][j] + " ";
            }
            s += data[i][classAttribute.getIndex()] + "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        Data data = new Data();
        System.out.println("Prima del sort:");
        System.out.println(data.toString());

        data.sort(data.getExplanatoryAttribute(0), 0, data.getNumberOfExamples() - 1);

        System.out.println("Dopo il sort per motor:");
        System.out.println(data.toString());
    }

    private void swap(int i, int j) {
    Object[] temp = (Object[]) data[i];
    data[i] = data[j];
    data[j] = temp;
}

public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
    int attrIndex = attribute.getIndex();
    for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
        Object[] key = (Object[]) data[i];
        String keyVal = (String) key[attrIndex];
        int j = i - 1;
        while (j >= beginExampleIndex && 
               ((String) data[j][attrIndex]).compareTo(keyVal) > 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = key;
    }
}
}