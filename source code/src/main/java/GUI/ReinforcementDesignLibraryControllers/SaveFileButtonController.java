package GUI.ReinforcementDesignLibraryControllers;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import SLS.Sls;
import diagnosis.DiagnosisMainAlgorithm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import mainalgorithm.InternalForces;
import mainalgorithm.Reinforcement;
import materials.Concrete;
import materials.DimensionsOfCrossSectionOfConcrete;
import materials.Steel;
import util.ResultsToPDF;

public class SaveFileButtonController {

	// design scene

	private static Button designSaveButton;

	public static void addPropertiesToDesignSceneSaveButton(Button bt, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) {
		designSaveButton = bt;
		disableButtonDesign(bt);
		addClickListenerToDesignSceneSaveButton(bt, concrete, steel, reinforcement, forces, dimensions, sls);
	}

	private static void disableButtonDesign(Button bt) {
		bt.setDisable(true);
	}

	protected static void enableSaveButtonDesignScene() {
		designSaveButton.setDisable(false);
	}

	private static void addClickListenerToDesignSceneSaveButton(Button bt, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) {
		bt.setOnAction(new SaveButtonListenerDesignScene(concrete, steel, reinforcement, forces, dimensions, sls));
	}

	private static class SaveButtonListenerDesignScene implements EventHandler<ActionEvent> {
		Reinforcement reinforcement;
		InternalForces forces;
		DimensionsOfCrossSectionOfConcrete dimensions;
		Concrete concrete;
		Steel steel;
		Sls sls;

		protected SaveButtonListenerDesignScene(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) {
			this.reinforcement = reinforcement;
			this.forces = forces;
			this.dimensions = dimensions;
			this.concrete = concrete;
			this.steel = steel;
			this.sls = sls;
		}

		@Override
		public void handle(ActionEvent arg0) {
			try {
				ResultsToPDF.saveDesingResultsToPDF(concrete, steel, reinforcement, forces, dimensions, sls);
			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// diagnosis scene

	private static Button diagnosisSaveButton;

	public static void addPropertiesToDiagnosisSceneSaveButton(Button bt, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls, DiagnosisMainAlgorithm diagnosis) {
		diagnosisSaveButton = bt;
		disableButtonDiagnosis(bt);
		addClickListenerToDiagnosisSceneSaveButton(bt, concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
	}

	private static void disableButtonDiagnosis(Button bt) {
		bt.setDisable(true);
	}

	protected static void enableButtonDiagnosis() {
		if (diagnosisSaveButton.isDisable()) {
			diagnosisSaveButton.setDisable(false);
		}
	}

	private static void addClickListenerToDiagnosisSceneSaveButton(Button bt, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls, DiagnosisMainAlgorithm diagnosis) {
		bt.setOnAction(new SaveButtonListenerDiagnosisScene(concrete, steel, reinforcement, forces, dimensions, sls, diagnosis));
	}

	private static class SaveButtonListenerDiagnosisScene implements EventHandler<ActionEvent> {
		Reinforcement reinforcement;
		InternalForces forces;
		DimensionsOfCrossSectionOfConcrete dimensions;
		Concrete concrete;
		Steel steel;
		Sls sls;
		DiagnosisMainAlgorithm diagnosis;

		protected SaveButtonListenerDiagnosisScene(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls,
				DiagnosisMainAlgorithm diagnosis) {
			this.reinforcement = reinforcement;
			this.forces = forces;
			this.dimensions = dimensions;
			this.concrete = concrete;
			this.steel = steel;
			this.sls = sls;
			this.diagnosis = diagnosis;
		}

		@Override
		public void handle(ActionEvent arg0) {

			try {
				ResultsToPDF.saveDiagnosisResultsToPDF(concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
