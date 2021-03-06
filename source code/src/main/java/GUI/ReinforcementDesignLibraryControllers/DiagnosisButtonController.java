package GUI.ReinforcementDesignLibraryControllers;

import SLS.Sls;
import SLS.creepCoeficent.CreepCoeficent;
import diagnosis.DiagnosisMainAlgorithm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import mainalgorithm.InternalForces;
import mainalgorithm.Reinforcement;
import mainalgorithm.RequiredReinforcement;
import materials.Cement;
import materials.Concrete;
import materials.DimensionsOfCrossSectionOfConcrete;
import materials.Steel;
import util.ResultsToPDF;

public class DiagnosisButtonController {

	public static void addPropertiesToDiagnosisButton(Button button, RequiredReinforcement requiredReinforcement, Concrete concrete, Steel steel, InternalForces internalForces,
			DimensionsOfCrossSectionOfConcrete dimensions, Reinforcement reinforcement, ResultsPaneControllerDiagnosis resultsPaneControllerDiagnosis, Cement cement, Sls sls, InternalForces forces,
			CreepCoeficent creep, DiagnosisMainAlgorithm diagnosisMainAlgorithm) {
		addListenerToDiagnosisButton(button, requiredReinforcement, concrete, steel, internalForces, dimensions, reinforcement, resultsPaneControllerDiagnosis, cement, sls, forces, creep,
				diagnosisMainAlgorithm);
	}

	private static void addListenerToDiagnosisButton(Button button, RequiredReinforcement requiredReinforcement, Concrete concrete, Steel steel, InternalForces internalForces,
			DimensionsOfCrossSectionOfConcrete dimensions, Reinforcement reinforcement, ResultsPaneControllerDiagnosis resultsPaneControllerDiagnosis, Cement cement, Sls sls, InternalForces forces,
			CreepCoeficent creep, DiagnosisMainAlgorithm diagnosisMainAlgorithm) {
		button.setOnAction(new PresedDiagnosisButton(requiredReinforcement, concrete, steel, internalForces, dimensions, reinforcement, resultsPaneControllerDiagnosis, cement, sls, forces, creep,
				diagnosisMainAlgorithm));
	}

	private static class PresedDiagnosisButton implements EventHandler<ActionEvent> {
		Concrete concrete;
		Steel steel;
		InternalForces internalForces;
		DimensionsOfCrossSectionOfConcrete dimensions;
		RequiredReinforcement requiredReinforcement;
		Reinforcement reinforcement;
		Cement cement;
		Sls sls;
		ResultsPaneControllerDiagnosis resultsPaneControllerDiagnosis;
		InternalForces forces;
		CreepCoeficent creep;
		DiagnosisMainAlgorithm diagnosisMainAlgorithm;

		protected PresedDiagnosisButton(RequiredReinforcement requiredReinforcement, Concrete concrete, Steel steel, InternalForces internalForces, DimensionsOfCrossSectionOfConcrete dimensions,
				Reinforcement reinforcement, ResultsPaneControllerDiagnosis resultsPaneControllerDiagnosis, Cement cement, Sls sls, InternalForces forces, CreepCoeficent creep,
				DiagnosisMainAlgorithm diagnosisMainAlgorithm) {
			this.requiredReinforcement = requiredReinforcement;
			this.concrete = concrete;
			this.steel = steel;
			this.internalForces = internalForces;
			this.dimensions = dimensions;
			this.reinforcement = reinforcement;
			this.resultsPaneControllerDiagnosis = resultsPaneControllerDiagnosis;
			this.cement = cement;
			this.sls = sls;
			this.forces = forces;
			this.creep = creep;
			this.diagnosisMainAlgorithm = diagnosisMainAlgorithm;

		}

		@Override
		public void handle(ActionEvent arg0) {
			ResultsToPDF.clearResults();
			requiredReinforcement.checkWhatIsRequiredReinforcement(concrete, steel, internalForces, dimensions, reinforcement);
			sls.runSLS(concrete, cement, steel, dimensions, creep, reinforcement, forces);
			diagnosisMainAlgorithm.runDiagnosis(concrete, steel, internalForces.getmEd(), internalForces.getnEd(), dimensions, reinforcement);
			resultsPaneControllerDiagnosis.dispResults();
			
			SaveFileButtonController.enableButtonDiagnosis();
		}
	}

}
