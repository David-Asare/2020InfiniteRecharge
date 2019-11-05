/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
0 - PDP (power distrubution panel)
1 - Front left drive
2 - Rear left drive
3 - Rear Right drive
4 - Front right drive
5 - Crawl motor (white wheels at front)
6 - Intake Left (green wheels)
7 - Intake Right (green wheels)
8 - Front lift (motors at bottom rack in back)
9 - Front lift (motors at bottom rack in back)
10 - Back lift
11 - Back lift
12 - PCM (pnuematic control module)
*/

package frc.robot;
import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  DifferentialDrive myRobot;  
  WPI_VictorSPX frontLeft,frontRight,rearLeft,rearRight;//motor for drive base

  Joystick driver = new Joystick(0);  
  Joystick operator = new Joystick(1);//logitech gamepad
    
  //Instantiates compressor so air can be used for pneumatic manipulators
  Compressor comp = new Compressor(12);                           
  boolean enabled = comp.enabled(); 
  boolean pressureSwitch = comp.getPressureSwitchValue();
  double current = comp.getCompressorCurrent();
         	
 	//Instantiates Solenoids for pneumatic grabber and LEDs
  DoubleSolenoid hardWheelSol = new DoubleSolenoid(12,1,0); 
  DoubleSolenoid hatchPanel = new DoubleSolenoid(12, 3, 2);
  DoubleSolenoid frontDrop = new DoubleSolenoid(12, 5, 4);

   //Instantiates motors for manipulators
  WPI_VictorSPX Intake = new WPI_VictorSPX(7);
  WPI_VictorSPX LIntake = new WPI_VictorSPX(6);
  WPI_VictorSPX Elevator = new WPI_VictorSPX(8);
  WPI_VictorSPX ElevatorFollower = new WPI_VictorSPX(9);
  WPI_VictorSPX backLift = new WPI_VictorSPX(10);
  WPI_VictorSPX backLiftFollower = new WPI_VictorSPX(11);
  WPI_VictorSPX crawlMotor = new WPI_VictorSPX(5);// moves robot forward when lift is extended


  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture(0);
        comp.setClosedLoopControl(true);
     	
    frontLeft = new WPI_VictorSPX(1);
    rearLeft = new WPI_VictorSPX(2);
    frontRight = new WPI_VictorSPX(4);
    rearRight = new WPI_VictorSPX(3);
     	
    frontLeft.setInverted(true);
    frontRight.setInverted(true);

    SpeedControllerGroup mLeft = new SpeedControllerGroup(frontLeft,rearLeft);
    SpeedControllerGroup mRight = new SpeedControllerGroup(frontRight,rearRight);
    myRobot = new DifferentialDrive(mLeft,mRight);
    //ElevatorFollower.set(ControlMode.Follower,7);//sets elevator to follow motor @ CAN ID 7
       
    //sets up intake motors
    Intake.setSafetyEnabled(true);                             
    Intake.setExpiration(.1);
    LIntake.setSafetyEnabled(true);                             
    LIntake.setExpiration(.1);



    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
    super.teleopInit();
    Elevator.disable();
    Elevator.setSafetyEnabled(true);
    Elevator.setExpiration(.1);
    ElevatorFollower.disable();
    ElevatorFollower.setSafetyEnabled(true);
    ElevatorFollower.setExpiration(.1);
    backLift.disable();
    backLift.setSafetyEnabled(true);
    backLift.setExpiration(.1);
    backLiftFollower.disable();
    backLiftFollower.setSafetyEnabled(true);
    backLiftFollower.setExpiration(.1);

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
            //Operator operations
    //setting joystick for pneumatics outward
    if(operator.getRawAxis(1)>0.3)
    {
      Intake.set(0.5*operator.getRawAxis(1));
      LIntake.set(-0.5*operator.getRawAxis(1));
    }
    else
      //pneumatics inward
      if(operator.getRawAxis(1)<-0.3)
      {
        Intake.set(-0.5*operator.getRawAxis(1));
        LIntake.set(0.5*operator.getRawAxis(1));
      }
      else
      {
        Intake.stopMotor();
        LIntake.stopMotor();
      }

    //setting panel to going out
    if(operator.getRawButton(3)) //x-button
      hatchPanel.set(DoubleSolenoid.Value.kReverse);
    else
    //setting panel going forward
      if(operator.getRawButton(1)) //a-button
        hatchPanel.set(DoubleSolenoid.Value.kForward);

        //moving front lift
    if(Math.abs(operator.getRawAxis(3)) > 0.1) //right trigger
    {
      //elevator downward
      Elevator.set(-1);
      ElevatorFollower.set(-1);
    }
    else
      //elevator upward
      if(operator.getRawButton(6))  //right bumper
      { 
        Elevator.set(1);
        ElevatorFollower.set(1);
      }
      else
      {
        Elevator.stopMotor();
        ElevatorFollower.stopMotor();
      }
      
      //moving back lift
    if(operator.getRawAxis(2) > 0.1) //left trigger
    {
      Elevator.set(-1);  //move elevator down
      ElevatorFollower.set(1);
    }
    else
      //elevator upward
      if(operator.getRawButton(5)) //left bumper
      { 
        backLift.set(1);
        backLiftFollower.set(-1);
      }
      else
      {
        backLift.stopMotor();
        backLiftFollower.stopMotor();
      }


      //Driver operations
      //driver turning on and off
    if(driver.getRawButton(8))
      comp.start();
    if(driver.getRawButton(9))
      comp.stop();

      //getting robot on platform
    if(driver.getRawButton(1))
      crawlMotor.set(-1);
    else
      crawlMotor.stopMotor();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
