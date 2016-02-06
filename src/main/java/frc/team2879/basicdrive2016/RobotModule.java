package frc.team2879.basicdrive2016;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;
import jaci.openrio.toast.lib.module.ModuleConfig;
import jaci.openrio.toast.lib.registry.Registrar;

public class RobotModule extends IterativeModule {

    public static Logger logger;
    
    public static ModuleConfig pref;
    
    public static String DRIVE_TYPE;
    public static boolean DRIVE_SQUAREDINPUTS;
    public static boolean DRIVE_BRAKE;
    
    public RobotDrive robotDrive;
    
    public final XboxController driveJoystick = new XboxController(0);
    
    public CANTalon leftTalon = Registrar.canTalon(1);
    public CANTalon leftTalonF = Registrar.canTalon(2);
    public CANTalon rightTalon = Registrar.canTalon(3);
    public CANTalon rightTalonF = Registrar.canTalon(4);

    @Override
    public String getModuleName() {
        return "BasicDrive2016";
    }

    @Override
    public String getModuleVersion() {
        return "0.0.4";
    }
    
    @Override
    public void robotInit() {
        logger = new Logger("BasicDrive2016", Logger.ATTR_DEFAULT);
        //TODO: Module Init
        
        pref = new ModuleConfig("BasicDrive2016");
        DRIVE_TYPE = pref.getString("drive.type", "TANK");
        DRIVE_SQUAREDINPUTS = pref.getBoolean("drive.squaredinputs", false);
        DRIVE_BRAKE = pref.getBoolean("drive.brake", false);
        
        SmartDashboard.putString("Drive Type", DRIVE_TYPE);
        SmartDashboard.putBoolean("Squared Inputs", DRIVE_SQUAREDINPUTS);
        SmartDashboard.putBoolean("Brake", DRIVE_BRAKE);
        
        leftTalon.changeControlMode(TalonControlMode.PercentVbus);
        rightTalon.changeControlMode(TalonControlMode.PercentVbus);
        leftTalonF.changeControlMode(TalonControlMode.Follower);
        rightTalonF.changeControlMode(TalonControlMode.Follower);
        
        leftTalonF.set(leftTalon.getDeviceID());
        rightTalonF.set(rightTalon.getDeviceID());
                        
        leftTalon.enableBrakeMode(DRIVE_BRAKE);
        leftTalonF.enableBrakeMode(DRIVE_BRAKE);
        rightTalon.enableBrakeMode(DRIVE_BRAKE);
        rightTalonF.enableBrakeMode(DRIVE_BRAKE);

        leftTalon.set(0);
        rightTalon.set(0);
        
        robotDrive = new RobotDrive(leftTalon, rightTalon);
                
        robotDrive.setSafetyEnabled(false);        
    }
    
    public void teleopPeriodic() {
            if(DRIVE_TYPE.toLowerCase().equals("tank")) {
                robotDrive.tankDrive(driveJoystick.leftStick.getY(), driveJoystick.rightStick.getY(), DRIVE_SQUAREDINPUTS);
            } else if (DRIVE_TYPE.toLowerCase().equals("arcade")) {
                robotDrive.arcadeDrive(driveJoystick.leftStick.getY(), driveJoystick.leftStick.getX(), DRIVE_SQUAREDINPUTS);
            }
    }
}
