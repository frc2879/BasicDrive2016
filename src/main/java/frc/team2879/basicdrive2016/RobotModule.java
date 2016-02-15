package frc.team2879.basicdrive2016;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.openrio.toast.core.thread.Heartbeat;
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
    
    public static Integer[] TALONS_LEFT;
    public static Integer[] TALONS_RIGHT;
    
    public static boolean TALONS_REVERSE_LEFT;
    public static boolean TALONS_REVERSE_RIGHT;
    
    public static final String driveTank = "TANK";
    public static final String driveArcade = "ARCADE";
    public static final String driveArcade2Stick = "ARCADE2STICK";
    
    private SendableChooser driveChooser;
    
    public RobotDrive robotDrive;
    
    public final XboxController driveJoystick = new XboxController(0);
    
    public CANTalon leftTalon;
    public CANTalon leftTalonF;
    public CANTalon rightTalon;
    public CANTalon rightTalonF;

    @Override
    public String getModuleName() {
        return "BasicDrive2016";
    }

    @Override
    public String getModuleVersion() {
        return "0.0.5";
    }
    
    @Override
    public void robotInit() {
        logger = new Logger("BasicDrive2016", Logger.ATTR_DEFAULT);
        //TODO: Module Init
        
        pref = new ModuleConfig("BasicDrive2016");
        DRIVE_TYPE = pref.getString("drive.type", driveTank);
        DRIVE_SQUAREDINPUTS = pref.getBoolean("drive.squaredinputs", false);
        DRIVE_BRAKE = pref.getBoolean("drive.brake", false);
        
        TALONS_LEFT = (Integer[]) pref.getArray("talons.left", new Integer[] {1, 2,});
        TALONS_RIGHT = (Integer[]) pref.getArray("talons.right", new Integer[] {3, 4});
        
        TALONS_REVERSE_LEFT = pref.getBoolean("talons.reverse.left", false);
        TALONS_REVERSE_RIGHT = pref.getBoolean("talons.reverse.right", false);
        
        driveChooser = new SendableChooser();
        driveChooser.addDefault("Tank", driveTank);
        driveChooser.addObject("Arcade", driveArcade);
        driveChooser.addObject("Arcade 2 Stick", driveArcade2Stick);
        SmartDashboard.putData("Drive Chooser", driveChooser);
        
        SmartDashboard.putString("Drive Type", DRIVE_TYPE);
        SmartDashboard.putBoolean("Squared Inputs", DRIVE_SQUAREDINPUTS);
        SmartDashboard.putBoolean("Brake", DRIVE_BRAKE);
        
        Heartbeat.add(skipped -> {
            if(!(((String) driveChooser.getSelected()).equalsIgnoreCase(DRIVE_TYPE))) {
                DRIVE_TYPE = (String) driveChooser.getSelected();
                SmartDashboard.putString("Drive Type", DRIVE_TYPE);
            }
        });
        
        leftTalon = Registrar.canTalon(TALONS_LEFT[0]);
        leftTalonF = Registrar.canTalon(TALONS_LEFT[1]);
        rightTalon = Registrar.canTalon(TALONS_RIGHT[0]);
        rightTalonF = Registrar.canTalon(TALONS_RIGHT[1]);
        
        leftTalon.changeControlMode(TalonControlMode.PercentVbus);
        rightTalon.changeControlMode(TalonControlMode.PercentVbus);
        leftTalonF.changeControlMode(TalonControlMode.Follower);
        rightTalonF.changeControlMode(TalonControlMode.Follower);
        
        
        leftTalonF.set(leftTalon.getDeviceID());
        rightTalonF.set(rightTalon.getDeviceID());
        
        leftTalon.setInverted(TALONS_REVERSE_LEFT);
        rightTalon.setInverted(TALONS_REVERSE_RIGHT);
        
                        
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
            if(DRIVE_TYPE.equalsIgnoreCase(driveTank)) {
                robotDrive.tankDrive(driveJoystick.leftStick.getY(), driveJoystick.rightStick.getY(), DRIVE_SQUAREDINPUTS);
            } else if (DRIVE_TYPE.equalsIgnoreCase(driveArcade)) {
                robotDrive.arcadeDrive(driveJoystick.leftStick.getY(), driveJoystick.leftStick.getX(), DRIVE_SQUAREDINPUTS);
            } else if (DRIVE_TYPE.equalsIgnoreCase(driveArcade2Stick)) {
                robotDrive.arcadeDrive(driveJoystick.leftStick.getY(), driveJoystick.rightStick.getX(), DRIVE_SQUAREDINPUTS);
            }
    }
}
