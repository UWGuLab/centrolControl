package centrolControl;


import ij.WindowManager;
import ij.gui.ImageWindow;
import java.util.Scanner;
import org.micromanager.MMStudio;
import org.micromanager.SnapLiveManager;
import org.micromanager.acquisition.AcquisitionEngine;

import mmcorej.CMMCore;
import mmcorej.Configuration;
import mmcorej.PropertySetting;
import mmcorej.StrVector;
import org.micromanager.api.MultiStagePosition;
import org.micromanager.api.PositionList;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.micromanager.imagedisplay.VirtualAcquisitionDisplay;

public class main {

    public static void main(String[] args) throws Exception {

        //Fluidic Control
        Fluidic experiment = new Fluidic();
        experiment.getSelector().getCurrentPosition();
        experiment.wash();
        experiment.injectBuffer();
        experiment.startIncorp0();

//        /*
//         * load user interface
//         */
//        //Imaging System Control
//        MMStudio gui_ = new MMStudio(false);
////        CMMCore core = gui_.getCore();
//
//        // start acquisition engine
////        AcquisitionEngine engine_ = gui_.getAcquisitionEngine();
//
//        //open Acquisition Control Dialog for cycle 0
////        gui_.openAcqControlDialog();
//        gui_.loadAcquisition("G:\\micro manager control\\AcqSettings20180423.xml");
//
//        PositionList positionList = gui_.getPositionList();
//        positionList.load("C:\\Users\\Nikon\\Desktop\\Micromanager_test\\20180327_test_multiD\\20180424_Donny_PositionList_mammal_kidney.pos");
//
//        gui_.runAcquisition("Incorp", "C:\\Users\\Nikon\\Desktop\\Micromanager_test\\20180423");
////        ImageWindow activeWindow = WindowManager.getCurrentWindow();
////        String acqName = activeWindow.getTitle();
////        VirtualAcquisitionDisplay.getDisplay(activeWindow.getImagePlus()).promptToSave(false);
////        gui_.closeAcquisitionWindow(acqName);
////        gui_.closeAcquisition(acqName);
//        Thread.sleep(30000);
//        gui_.runAcquisition("Incorp", "C:\\Users\\Nikon\\Desktop\\Micromanager_test\\20180423");
    }
}
