package lk.ijse.parameeIceCream.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFieldValid(TextField textField, String text){
        String filed = "";

        switch (textField){
            case CID:
                filed = "^([C][0-9]{3,10})$";
                break;
            case DID:
                filed = "^([D][0-9]{3,10})$";
                break;
            case EID:
                filed = "^([E][0-9]{3,10})$";
                break;
            case IID:
                filed = "^([I][0-9]{3,10})$";
                break;
            case MID:
                filed = "^([M][0-9]{3,10})$";
                break;
            case OID:
                filed = "^([O][0-9]{3,10})$";
                break;
            case PID:
                filed = "^([P][0-9]{3,10})$";
                break;
            case SID:
                filed = "^([S][0-9]{3,10})$";
                break;
            case NAME:
                filed = "^[A-Za-z]{3,}(?:\\\\s[A-Za-z]{3,})?$";
                break;
            case NIC:
                filed = "^([0-9]{8}[x|X|v|V]|[0-9]{12})$";
                break;
            case EMAIL:
                filed = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case PASSWORD:
                filed = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                break;
            case TEL:
                filed = "^\\d{10}$";
                break;
            case ADDRESS:
                filed = "^([A-z0-9]|[-/,.@+]|\\\\s){4,}$";
                break;
            case PRICE:
                filed = "^([0-9]){1,}[.]([0-9]){1,}$";
                break;
            case QTY:
                filed = "^\\d+$";
                break;
        }

        Pattern pattern = Pattern.compile(filed);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean setTextColor(TextField location, javafx.scene.control.TextField textField){
        if (Regex.isTextFieldValid(location, textField.getText())){
            textField.setStyle("-fx-border-color: green;");
            return true;
        }else {
            textField.setStyle("-fx-border-color: red;");
            return false;
        }
    }
}
