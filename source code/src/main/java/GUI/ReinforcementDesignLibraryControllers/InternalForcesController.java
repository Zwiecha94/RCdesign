package GUI.ReinforcementDesignLibraryControllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import mainalgorithm.InternalForces;
import util.InputValidation;
import util.StringToDouble;

public class InternalForcesController {
	
	///3.0 ver
	
	public static void addPropertiesToTextField(InternalForces internalForces, TextField tf) {
		addInputListener(internalForces, tf);
		addFocusListener(tf);
		setInitialValue(tf);
	}
	
	///////

	public static void addPropertiesToMEdTextField(InternalForces internalForces, TextField mEd, TextField mEdCharCalk,
			TextField mEdCharDlug) {
		addInputMEdListener(internalForces, mEd);
		addFocusMEdListener(mEd, mEdCharCalk, mEdCharDlug);
		setMedInitialValue(mEd);
	}

	public static void addPropertiesToMEdCharCalk(InternalForces forces, TextField mEd, TextField mEdCharCalk,
			TextField mEdCharDlug) {
		addInputMEdCharCalListener(forces, mEdCharCalk);
		addFocusMEdCharCalListener(mEd, mEdCharCalk, mEdCharDlug);
		setMedInitialValue(mEdCharCalk);
	}

	public static void addPropertiesToMEdCharDlug(InternalForces forces, TextField mEd, TextField mEdCharCalk,
			TextField mEdCharDlug) {
		addInputMEdCharDlugListener(forces, mEdCharDlug);
		addFocusMEdCharDlugListener(mEdCharDlug);
		setMedInitialValue(mEdCharDlug);
	}

	public static void addPropertiesToNEdTextField(InternalForces internalForces, TextField nEd,
			ChoiceBox<String> crossSectionTypeChoiceBox) {
		addInputNEdListener(internalForces, nEd, crossSectionTypeChoiceBox);
		addFocusNEdListener(nEd);
		setNedInitialValue(nEd);
	}

	public static void addPropertiesToVEdTextField(InternalForces internalForces, TextField vEd) {
		addInputVEdListener(internalForces, vEd);
		addFocusVEdListener(vEd);
		setVedInitialValue(vEd);
	}

	// mEd text field

	private static void addInputMEdListener(InternalForces internalForces, TextField mEd) {
		mEd.textProperty().addListener(new MEdInputListener(internalForces));
	}

	private static boolean isMEdInputCorrect;
	private static double mEdValue;
	private static String mEdStringValue;

	private static class MEdInputListener implements ChangeListener<String> {
		private InternalForces internalForces;

		private MEdInputListener(InternalForces internalForces) {
			this.internalForces = internalForces;
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

			// System.out.println(arg2);
			arg2 = arg2.replaceAll(",", ".");
			if (InputValidation.mEdInputValidation(arg2)) {
				internalForces.setmEd(StringToDouble.stringToDouble(arg2));
				isMEdInputCorrect = true;
				mEdValue = Double.parseDouble(arg2);
				mEdStringValue = arg2;
			} else {
				isMEdInputCorrect = false;
				mEdValue = 0.0;
				mEdStringValue = "0";
			}

		}

	}

	private static void addFocusMEdListener(TextField mEd, TextField mEdCharCalk, TextField mEdCharDlug) {
		mEd.focusedProperty().addListener(new MEdFocusListener(mEd, mEdCharCalk, mEdCharDlug));
	}

	private static class MEdFocusListener implements ChangeListener<Boolean> {

		TextField mEd;
		TextField mEdCharCalk;
		TextField mEdCharDlug;

		protected MEdFocusListener(TextField mEd, TextField mEdCharCalk, TextField mEdCharDlug) {
			this.mEd = mEd;
			this.mEdCharCalk = mEdCharCalk;
			this.mEdCharDlug = mEdCharDlug;
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isMEdInputCorrect == false) {
				setMedInitialValue(mEd);
			}
		}

		private void fuction() {
			if (mEdValue < mEdCharCalValue) {
				mEdCharCalk.setText(mEdStringValue);
				if (mEdCharCalValue < mEdCharDlugValue) {
					mEdCharDlug.setText(mEdCharCalStringValue);
				}
			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}
			fuction();
		}

	}

	private static void addInputNEdListener(InternalForces internalForces, TextField nEd,
			ChoiceBox<String> crossSectionTypeChoiceBox) {
		nEd.textProperty().addListener(new NEdInputListener(internalForces, crossSectionTypeChoiceBox, nEd));
	}

	// mEdCharCalk Mek textfield

	private static void addInputMEdCharCalListener(InternalForces internalForces, TextField mEdCharObl) {
		mEdCharObl.textProperty().addListener(new MEdCharCalInputListener(internalForces));
	}

	private static boolean isMEdCharCalInputCorrect;
	private static double mEdCharCalValue;
	private static String mEdCharCalStringValue;

	private static class MEdCharCalInputListener implements ChangeListener<String> {
		private InternalForces internalForces;

		private MEdCharCalInputListener(InternalForces internalForces) {
			this.internalForces = internalForces;
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			arg2 = arg2.replaceAll(",", ".");
			if (InputValidation.mEdInputValidation(arg2)) {
				internalForces.setCharacteristicMEdCalkowita(StringToDouble.stringToDouble(arg2));
				isMEdCharCalInputCorrect = true;
				mEdCharCalValue = Double.parseDouble(arg2);
				mEdCharCalStringValue = arg2;
			} else {
				isMEdCharCalInputCorrect = false;
				mEdCharCalValue = 0.0;
				mEdCharCalStringValue = "0";
			}

		}
	}

	private static void addFocusMEdCharCalListener(TextField mEd, TextField mEdCharCalk, TextField mEdCharDlug) {
		mEdCharCalk.focusedProperty().addListener(new MEdCharCalFocusListener(mEd, mEdCharCalk, mEdCharDlug));
	}

	private static class MEdCharCalFocusListener implements ChangeListener<Boolean> {
		TextField mEd;
		TextField mEdCharCalk;
		TextField mEdCharDlug;

		protected MEdCharCalFocusListener(TextField mEd, TextField mEdCharCalk, TextField mEdCharDlug) {
			this.mEd = mEd;
			this.mEdCharCalk = mEdCharCalk;
			this.mEdCharDlug = mEdCharDlug;
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isMEdCharCalInputCorrect == false) {
				setMedInitialValue(mEdCharCalk);
			}
		}

		private void ifMekIsGreaterThenMedChangeValueToEqual() {
			if (mEdCharCalValue > mEdValue) {
				mEdCharCalk.setText(mEdStringValue);

			}
		}

		private void function() {
			if (mEdCharCalValue < mEdCharDlugValue && mEdCharCalValue > 0) {
				mEdCharDlug.setText(mEdCharCalStringValue);

			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
			ifMekIsGreaterThenMedChangeValueToEqual();
			function();
			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}

		}

	}

	// mEdCharDlug Mek,lt textField

	private static void addInputMEdCharDlugListener(InternalForces internalForces, TextField mEdCharDlug) {
		mEdCharDlug.textProperty().addListener(new MEdCharDlugInputListener(internalForces));
	}

	private static boolean isMEdCharDlugInputCorrect;
	private static double mEdCharDlugValue;
	private static String mEdCharString;

	private static class MEdCharDlugInputListener implements ChangeListener<String> {
		private InternalForces internalForces;

		private MEdCharDlugInputListener(InternalForces internalForces) {
			this.internalForces = internalForces;
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			arg2 = arg2.replaceAll(",", ".");
			if (InputValidation.mEdInputValidation(arg2)) {
				internalForces.setCharacteristicMEdDlugotrwale(StringToDouble.stringToDouble(arg2));
				isMEdCharDlugInputCorrect = true;
				mEdCharDlugValue = Double.parseDouble(arg2);
				mEdCharString = arg2;
			} else {
				isMEdCharDlugInputCorrect = false;
				mEdCharDlugValue = 0.0;
				mEdCharString = "0";
			}

		}
	}

	private static void addFocusMEdCharDlugListener(TextField mEd) {
		mEd.focusedProperty().addListener(new MEdCharDlugFocusListener(mEd));
	}

	private static class MEdCharDlugFocusListener implements ChangeListener<Boolean> {

		TextField med;

		protected MEdCharDlugFocusListener(TextField mEd) {
			this.med = mEd;
		}

		private void compareMedCharAndCal() {
			if (mEdCharDlugValue > mEdCharCalValue) {
				med.setText(mEdCharCalStringValue);
			}
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isMEdCharDlugInputCorrect == false) {
				setMedInitialValue(med);
			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {

			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}
			compareMedCharAndCal();
		}

	}

	// nEd text field

	private static boolean isNEdInputCorrect;

	private static class NEdInputListener implements ChangeListener<String> {
		private InternalForces internalForces;
		private ChoiceBox<String> crossSectionTypeChoiceBox;
		private TextField nEd;

		private NEdInputListener(InternalForces internalForces, ChoiceBox<String> crossSectionTypeChoiceBox,
				TextField nEd) {
			this.internalForces = internalForces;
			this.crossSectionTypeChoiceBox = crossSectionTypeChoiceBox;
			this.nEd = nEd;
		}

		private void disableCbWhenNedIsNotEqual0() {
			crossSectionTypeChoiceBox.setDisable(true);
			crossSectionTypeChoiceBox.setValue("Przekrój prostokątny");
		}

		private void enableCbWhenNedIsEqual0() {
			crossSectionTypeChoiceBox.setDisable(false);
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			arg2 = arg2.replace(',', '.');
			if (InputValidation.internalForcesTextFieldInputValidation(arg2)) {
				internalForces.setnEd(StringToDouble.stringToDouble(arg2));
				if (nEd.textProperty().getValue().equals("0")) {
					enableCbWhenNedIsEqual0();
				} else {
					disableCbWhenNedIsNotEqual0();
				}
				isNEdInputCorrect = true;
			} else {
				isNEdInputCorrect = false;
			}

		}

	}

	private static void addFocusNEdListener(TextField nEd) {
		nEd.focusedProperty().addListener(new NEdFocusListener(nEd));
	}

	private static class NEdFocusListener implements ChangeListener<Boolean> {

		TextField ned;

		protected NEdFocusListener(TextField nEd) {
			this.ned = nEd;
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isNEdInputCorrect == false) {
				setNedInitialValue(ned);
			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}

		}

	}

	// vEd text field
	private static boolean isVEdInputCorrect;

	private static void addInputVEdListener(InternalForces internalForces, TextField vEd) {
		vEd.textProperty().addListener(new VEdInputListener(internalForces));
	}

	private static class VEdInputListener implements ChangeListener<String> {
		private InternalForces internalForces;

		private VEdInputListener(InternalForces internalForces) {
			this.internalForces = internalForces;
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			arg2 = arg2.replace(',', '.');
			if (InputValidation.vEdTextFieldInputValidation(arg2)) {
				internalForces.setvEd(StringToDouble.stringToDouble(arg2));
				isVEdInputCorrect = true;
			} else {
				isVEdInputCorrect = false;
			}

		}

	}

	private static void addFocusVEdListener(TextField vEd) {
		vEd.focusedProperty().addListener(new VEdFocusListener(vEd));
	}

	private static class VEdFocusListener implements ChangeListener<Boolean> {

		private TextField vEd;

		protected VEdFocusListener(TextField vEd) {
			this.vEd = vEd;
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isVEdInputCorrect == false) {
				setNedInitialValue(vEd);
			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}
		}

	}

	private static void setMedInitialValue(TextField mEd) {
		mEd.setText("0");
	}

	private static void setNedInitialValue(TextField nEd) {
		nEd.setText("0");
	}

	private static void setVedInitialValue(TextField vEd) {
		vEd.setText("0");
	}

	////// Ogolna metoda na sprawdzanie wprowadzanych danych

	// text field
	private static boolean isInputCorrect;

	private static void addInputListener(InternalForces internalForces, TextField tf) {
		tf.textProperty().addListener(new InputListener(internalForces, tf));
	}

	private static class InputListener implements ChangeListener<String> {
		private InternalForces internalForces;
		private TextField tf;

		private InputListener(InternalForces internalForces, TextField tf) {
			this.internalForces = internalForces;
			this.tf = tf;
		}

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			arg2 = arg2.replace(',', '.');
			if (InputValidation.internalForcesTextFieldInputValidation(arg2)) {
				String name = tf.getId();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method method = null;
				try {
					method = Class.forName("mainalgorithm.InternalForces").getMethod("set"+name, double.class);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				try {
					method.invoke(internalForces, StringToDouble.stringToDouble(arg2));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//internalForces.setvEd(StringToDouble.stringToDouble(arg2));
				isInputCorrect = true;
			} else {
				isInputCorrect = false;
			}

		}

	}

	private static void addFocusListener(TextField ForceEd) {
		ForceEd.focusedProperty().addListener(new FocusListener(ForceEd));
	}

	private static class FocusListener implements ChangeListener<Boolean> {

		private TextField ForceEd;

		protected FocusListener(TextField ForceEd) {
			this.ForceEd = ForceEd;
		}

		private void ifInputWasIncorrectSetValueToInitial() {
			if (isInputCorrect == false) {
				setInitialValue(ForceEd);
			}
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
			if (arg2 == false) {
				ifInputWasIncorrectSetValueToInitial();
			}
		}

	}
	
	private static void setInitialValue(TextField tf) {
		tf.setText("0");
	}

}
