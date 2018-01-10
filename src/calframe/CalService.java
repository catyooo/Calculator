package calframe;

class CalService {
	private boolean isSecondNum = false;
	private String lastOp;
	private String firstNum = "0";
	private String secondNum = "null";
	private double store;

    private String catNum(String cmd, String text) {
		String result = cmd;
		// 如果text不等于0
		if (!"0".equals(text)) {
			if (isSecondNum) {
				isSecondNum = false;
			} else {
				result = text + cmd;
			}
		}
		if (result.indexOf(".") == 0) {
			result = "0" + result;
		}
		return result;
	}

	private String setOp(String cmd, String text) {
		this.lastOp = cmd;
		this.firstNum = text;
		this.secondNum = null;

		this.isSecondNum = true;
		return null;
	}

	private String cal(String text, boolean isPercent) {
		double secondResult = secondNum == null ? Double.valueOf(text) : Double.valueOf(secondNum).doubleValue();

		//除数为0
		if(secondResult == 0 && this.lastOp.equals("/")){
			return "0";
		}

		//有%
		if(isPercent){
			secondResult = MyMath.multiply(Double.valueOf(firstNum), MyMath.divide(secondResult, 100));
		}
        switch (this.lastOp) {
            case "+":
                firstNum = String.valueOf(MyMath.add(Double.valueOf(firstNum), secondResult));
                break;
            case "-":
                firstNum = String.valueOf(MyMath.subtract(Double.valueOf(firstNum), secondResult));
                break;
            case "*":
                firstNum = String.valueOf(MyMath.multiply(Double.valueOf(firstNum), secondResult));
                break;
            case "/":
                firstNum = String.valueOf(MyMath.divide(Double.valueOf(firstNum), secondResult));
                break;
        }

		secondNum = secondNum == null ? text :secondNum;
		this.isSecondNum = true;
		return firstNum;
	}
	//求开方
    private String sqrt(String text){
		this.isSecondNum = true;
		return String.valueOf(Math.sqrt(Double.valueOf(text)));
	}
	//求倒数
    private String setReciprocal(String text){
		if (text.equals("0")){
			return text;
		}else{
			this.isSecondNum = true;
			return String.valueOf(MyMath.divide(1, Double.valueOf(text)));
		}
	}
	//存储
    private String mCmd(String cmd, String text){
        switch (cmd) {
            case "M+":
                store = MyMath.add(store, Double.valueOf(text));
                break;
            case "MC":
                store = 0;
                break;
            case "MR":
                isSecondNum = true;
                return String.valueOf(store);
            case "MS":
                store = Double.valueOf(text);
                break;
        }
		return null;
	}

	private String backSpace(String text){
		return text.equals("0") || text.equals("") ? "0" :text.substring(0,text.length()-1);
	}

	private String setNegative(String text){
		if(text.indexOf("-") == 0){
			return text.substring(1,text.length());
		}else{
			return "-" + text;
		}
	}
	private String clearAll(){
		this.firstNum = "0";
		this.secondNum = null;
		return this.firstNum;
	}
	private String clear(){
		return "0";
	}

	String callMethod(String cmd, String text){
        String numString = "0123456789.";
        String opString = "+-*/";
        if(cmd.equals("C")){
			return clearAll();
		}else if(cmd.equals("CE")){
			return clear();
		}else if (cmd.equals("Back")) {
			return backSpace(text);
		}else if (numString.contains(cmd)) {
			return catNum(cmd, text);
		}else if (opString.contains(cmd)) {
			return setOp(cmd, text);
		}else if (cmd.equals("+/-")) {
			return setNegative(text);  //设置正负号
		}else if(cmd.equals("1/x")){
			return setReciprocal(text);
		}else if (cmd.equals("sqrt")) {
			return sqrt(text);
		}else if(cmd.equals("%")){
			return cal(text, true);
		}else if(cmd.equals("=")){
			return cal(text, false);
		}else {
			return mCmd(cmd, text);
		}
	}
}
