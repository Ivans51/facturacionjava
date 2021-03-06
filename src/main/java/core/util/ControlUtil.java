package core.util;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class ControlUtil {

    public static void spinnerValue(int[] initialValue, Spinner<Integer>... spinner) {
        for (int i = 0; i < spinner.length; i++) {
            SpinnerValueFactory<Integer> valueFactory = //
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, initialValue[i]);
            spinner[i].setValueFactory(valueFactory);
        }
    }

    public static String removeDecimal(double doubles){
        return Double.toString(Math.floor(doubles));
    }
}