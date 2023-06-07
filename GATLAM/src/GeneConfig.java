import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("rawtypes")
class GeneConfig<T>{
    public final GeneDataType geneDataType;
    private final T maxValue;
    private final T minValue;
    public final T[] invalidValues;
    public final Class dataType;

    public T maxValue(){
        return maxValue;
    }

    public T minValue(){
        return minValue;
    }
    
    @SuppressWarnings("unchecked")
    public GeneConfig(JSONObject jsonObject){
        Object res = null;
        try{
            String line = (String)jsonObject.get("geneDataType");
            for(GeneDataType geneDT: GeneDataType.values()){
                if(geneDT.name().equals(line))
                    res = geneDT;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            geneDataType = (GeneDataType)res;
        }

        switch (geneDataType) {
            case Integer:
                dataType = Integer.class;
                break;
                
            case Float:
                dataType = Float.class;
                break;

            default:
                dataType = Double.class;
                break;
        }

        try{
            res = geneDataType.convert(jsonObject.get("maxValue"));
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            maxValue = (T)res;
        }

        try{
            res = geneDataType.convert(jsonObject.get("minValue"));
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            minValue = (T)res;
        }

        Object[] resArr = null;
        try{
            JSONArray temp = ((JSONArray)jsonObject.get("invalidValues"));
            resArr = new Object[temp.size()];
            for(int i=0; i < resArr.length; i++) {
                resArr[i] = temp.get(i);
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            invalidValues = (T[])resArr;
        }

    }


    static GeneConfig getGeneConfig(JSONObject jsonObject){
        GeneDataType res = null;
        try{
            String line = (String)jsonObject.get("geneDataType");
            for(GeneDataType geneDT: GeneDataType.values()){
                if(geneDT.name().equals(line))
                    res = geneDT;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        switch (res) {
            case Integer:
                return new GeneConfig<Integer>(jsonObject);
                
            case Float:
                return new GeneConfig<Float>(jsonObject);

            default:
                return new GeneConfig<Double>(jsonObject);
        }
    }

    public T convertFromBin(String str){
        T val = geneDataType.convertFromBin(str); 
        return val;
    }

    public int numBits(){
        return geneDataType.numBits();
    }

    public String generateGene(){
        T val = geneDataType.randVal(maxValue, minValue);
        List<T> notAllowed = Arrays.asList(invalidValues);
        while(notAllowed.contains(val)){
            val = geneDataType.randVal(maxValue, minValue);
        }
        return geneDataType.convertToBinary(val);
    }

    @SuppressWarnings("unchecked")
    public boolean validate(T val){
        Comparable<T> compVal = (Comparable<T>)val;
        if(compVal.compareTo(minValue) < 0 || compVal.compareTo(maxValue) > 0)
            return false;

        for(T inValid: invalidValues){
            if(inValid.equals(val))
                return false;
        }

        return true;
    }

}

@SuppressWarnings("unchecked")
enum GeneDataType{
    Integer{
        @Override
        public Integer convertFromBin(String str) {
            Long l = Long.parseLong(str, 2);
            return l.intValue();
        }

        @Override
        public int numBits() {
            return 32;
        }

        @Override
        public Integer randVal(Object max, Object min) {
            return Config.random.nextInt((Integer)max - (Integer)min) + (Integer)min;
        }

        @Override
        public String convertToBinary(Object val) {
            String temp = java.lang.Integer.toBinaryString((java.lang.Integer)val);
            return pad(temp, numBits());
            //long l = java.lang.Integer.toUnsignedLong((java.lang.Integer)val);
            //return Long.toBinaryString(l);
        }

        @Override
        public Integer convert(Long val) throws IncorrectValueException {
            return val.intValue();
        }

        @Override
        public Integer convert(java.lang.Double val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }

        @Override
        public Integer convert(String val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }

        @Override
        public Integer convert(java.lang.Boolean val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }
    },
    Float{
        @Override
        public Float convertFromBin(String str) {
            int v = java.lang.Integer.parseInt(str,2);
            return java.lang.Float.intBitsToFloat(v);
        }

        @Override
        public int numBits() {
            return 32;
        }

        @Override
        public Float randVal(Object max, Object min) {
            return (Float)min + ((Float)max - (Float)min) * Config.random.nextFloat();
        }

        @Override
        public String convertToBinary(Object val) {
            int v = java.lang.Float.floatToIntBits((Float)val);
            return pad(Integer.convertToBinary(v), numBits());
        }

        @Override
        public Float convert(Long val) {
            int v = val.intValue();
            return (float)v;
        }

        @Override
        public Float convert(java.lang.Double val) {
            double v = val;
            return (float)v;
        }

        @Override
        public Float convert(String val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }

        @Override
        public Float convert(java.lang.Boolean val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }
    },
    Double{
        @Override
        public Double convertFromBin(String str) {
            String temp = "0" + str.substring(1);
            Long v = Long.valueOf(temp, 2);
            if(str.charAt(0) == '1')
                return -1*java.lang.Double.longBitsToDouble(v);
            return java.lang.Double.longBitsToDouble(v);
        }

        @Override
        public int numBits() {
            return 64;
        }

        @Override
        public Double randVal(Object max, Object min) {
            return (Double)min + ((Double)max - (Double)min) * Config.random.nextDouble();
        }

        @Override
        public String convertToBinary(Object val) {
            long v = java.lang.Double.doubleToLongBits((Double)val);
            return pad(java.lang.Long.toBinaryString(v), numBits());
        }

        @Override
        public Double convert(Long val) {
            int v = val.intValue();
            return (double)v;
        }

        @Override
        public Double convert(java.lang.Double val){
            return val;
        }

        @Override
        public Double convert(String val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }

        @Override
        public Double convert(java.lang.Boolean val) throws IncorrectValueException {
            throw new IncorrectValueException(val, this);
        }
    };

    public abstract <T> T convertFromBin(String str);

    public abstract int numBits();

    public abstract <T> T randVal(Object max, Object min);

    public abstract String convertToBinary(Object val);

    public abstract <T> T convert(Long val) throws IncorrectValueException;

    public abstract <T> T convert(Double val) throws IncorrectValueException;

    public abstract <T> T convert(String val) throws IncorrectValueException;

    public abstract <T> T convert(Boolean val) throws IncorrectValueException;

    public <T> T convert(Object val) throws IncorrectValueException{
        if(val instanceof Long)
            return convert((Long)val);
        if(val instanceof java.lang.Double)
            return convert((java.lang.Double)val);
        if(val instanceof String)
            return convert((String)val);
        if(val instanceof Boolean)
            return convert((Boolean)val);
        throw new IncorrectValueException(val, this);
    }

    class IncorrectValueException extends Exception{
        public IncorrectValueException(Long val, GeneDataType geneDataType){
            super("Incorrect value: " + val + " for type: " + geneDataType.name());
        }

        public IncorrectValueException(Double val, GeneDataType geneDataType){
            super("Incorrect value: " + val.toString() + " for type: " + geneDataType.name());
        }

        public IncorrectValueException(String val, GeneDataType geneDataType){
            super("Incorrect value: " + val + " for type: " + geneDataType.name());
        }

        public IncorrectValueException(Boolean val, GeneDataType geneDataType){
            super("Incorrect value: " + val + " for type: " + geneDataType.name());
        }

        public IncorrectValueException(Object val, GeneDataType geneDataType){
            super("Incorrect value: " + val + " for type: " + geneDataType.name());
        }
    }

    private static String pad(String str, int numBits){
        String temp = str;
        while(temp.length() < numBits){
            temp = "0" + temp; 
        }
        return temp;
    }
}
