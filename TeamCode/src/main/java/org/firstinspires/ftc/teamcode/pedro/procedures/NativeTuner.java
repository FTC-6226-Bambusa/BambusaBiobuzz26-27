package org.firstinspires.ftc.teamcode.pedro.procedures;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

import java.util.List;
import java.util.Locale;

/*
* NOTE:
* This class was lowk vibe coded and is probably one of the few vibe coded things on this repo
* that's because the regular pedro pathing tuner wasn't working correctly and we genuinely tried
* everything to debug it but whether the tuner succeeded or not wasn't consistant and every time
* it ended up failing at seemingly random times.
*
* so yeah we just made ai translate the pedro pathing code which usually connects to the website and
* made it work directly on the telemetry so we could also see the values it was giving us,
* and it worked first try.
* */

// @TeleOp(name = "Pedro Pathing 3 Wheel Tuner", group = "Procedures")
public class NativeTuner extends LinearOpMode {

    private enum Stage {
        SETUP,
        LEFT_RESOLUTION,
        RIGHT_RESOLUTION,
        STRAFE_RESOLUTION,
        LEFT_OFFSET,
        RIGHT_OFFSET,
        TURN_MULTIPLIER,
        COMPLETE
    }

    private Stage currentStage = Stage.SETUP;

    // Adjustable tuning parameters
    private double pushDistance = 96.0;
    private int targetSpins = 3;

    // Hardware map names from Settings
    private final String leftEncoderName = Settings.LOCALIZATION.NAMES.LEFT;
    private final String rightEncoderName = Settings.LOCALIZATION.NAMES.RIGHT;
    private final String strafeEncoderName = Settings.LOCALIZATION.NAMES.STRAFE;
    private final String frontLeftName = Settings.DRIVE.NAMES.FL;
    private final String backLeftName = Settings.DRIVE.NAMES.BL;
    private final String frontRightName = Settings.DRIVE.NAMES.FR;
    private final String backRightName = Settings.DRIVE.NAMES.BR;

    // Hardware handles
    private DcMotorEx leftMotor, rightMotor, strafeMotor;
    private ThreeWheelConfig currentConfig;
    private ThreeWheelLocalizer localizer;

    // Tuning output variables
    private double leftTicksPerInch, rightTicksPerInch, strafeTicksPerInch;
    private double forwardTicksToInches, strafeTicksToInches;
    private double leftEncoderDirection = Encoder.FORWARD;
    private double rightEncoderDirection = Encoder.FORWARD;
    private double strafeEncoderDirection = Encoder.FORWARD;

    private List<Double> leftOffsets;  // [-x/pi, y/pi]
    private List<Double> rightOffsets; // [-x/pi, y/pi]
    private double turnTicksToRadians;
    private double strafePodX;

    // Turn stage tracking
    private double startHeading = 0.0;
    private String statusWarning = "";

    @Override
    public void runOpMode() {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        // Bulk caching and float drive wheels so the robot moves freely by hand
        initHardware();

        telemetry.addLine("=== PEDRO 3-WHEEL AUTOTUNER (BIOBUZZ) ===");
        telemetry.addLine("Press START on Driver Station to begin.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Update localizer if running
            Pose currentPose = new Pose(0, 0, 0);
            if (localizer != null) {
                localizer.update();
                currentPose = localizer.pose();
            }

            // Raw tick counts for hardware verification
            int rawL = (leftMotor != null) ? leftMotor.getCurrentPosition() : 0;
            int rawR = (rightMotor != null) ? rightMotor.getCurrentPosition() : 0;
            int rawS = (strafeMotor != null) ? strafeMotor.getCurrentPosition() : 0;

            telemetry.addLine("========================================");

            switch (currentStage) {
                // -----------------------------------------------------------
                // SETUP STAGE
                // -----------------------------------------------------------
                case SETUP:
                    telemetry.addLine("         [STAGE 0: PRE-FLIGHT SETUP]");
                    telemetry.addLine("========================================");
                    telemetry.addLine("Raw Encoders (hand spin to verify pods):");
                    telemetry.addLine(String.format(Locale.US, "  Left   (%s): %d", leftEncoderName, rawL));
                    telemetry.addLine(String.format(Locale.US, "  Right  (%s): %d", rightEncoderName, rawR));
                    telemetry.addLine(String.format(Locale.US, "  Strafe (%s): %d", strafeEncoderName, rawS));
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine(String.format(Locale.US, "Push Distance: %.1f\" (D-Pad UP/DN to change)", pushDistance));
                    telemetry.addLine(String.format(Locale.US, "Spin Count   : %d turns (D-Pad L/R to change)", targetSpins));
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS:");
                    telemetry.addLine("  [A] -> Begin Stage 1 (Left Pod Resolution)");

                    // Biobuzz SDK convenience button presses
                    if (gamepad1.dpadUpWasPressed()) pushDistance += 6.0;
                    if (gamepad1.dpadDownWasPressed() && pushDistance > 12.0) pushDistance -= 6.0;
                    if (gamepad1.dpadRightWasPressed()) targetSpins++;
                    if (gamepad1.dpadLeftWasPressed() && targetSpins > 1) targetSpins--;

                    if (gamepad1.aWasPressed()) {
                        rumble(150);
                        statusWarning = "";
                        enterStage(Stage.LEFT_RESOLUTION);
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 1: LEFT ENCODER RESOLUTION & DIRECTION
                // -----------------------------------------------------------
                case LEFT_RESOLUTION:
                    telemetry.addLine("   [STAGE 1/6: LEFT POD RESOLUTION]");
                    telemetry.addLine("========================================");
                    telemetry.addLine(String.format(Locale.US, "ACTION: Push robot FORWARD exactly %.1f\"", pushDistance));
                    telemetry.addLine("Keep straight! Do not rotate robot.");
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine(String.format(Locale.US, "Live Movement: %+.2f in", currentPose.x()));
                    telemetry.addLine(String.format(Locale.US, "Raw Encoders : L:%d | R:%d | S:%d", rawL, rawR, rawS));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS: [A] Lock In | [B] Reset Step");

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.LEFT_RESOLUTION);
                        statusWarning = "Step reset. Pose zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        double movement = currentPose.x();
                        if (Math.abs(movement) < 50.0) {
                            statusWarning = "Movement too small! Push robot before pressing [A].";
                            rumble(400);
                        } else {
                            leftTicksPerInch = Math.abs(movement / pushDistance);
                            leftEncoderDirection = (movement < 0 ? Encoder.REVERSE : Encoder.FORWARD);
                            rumble(150);
                            statusWarning = "";
                            enterStage(Stage.RIGHT_RESOLUTION);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 2: RIGHT ENCODER RESOLUTION & DIRECTION
                // -----------------------------------------------------------
                case RIGHT_RESOLUTION:
                    telemetry.addLine("  [STAGE 2/6: RIGHT POD RESOLUTION]");
                    telemetry.addLine("========================================");
                    telemetry.addLine(String.format(Locale.US, "ACTION: Push robot FORWARD exactly %.1f\"", pushDistance));
                    telemetry.addLine("Keep straight! Do not rotate robot.");
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine(String.format(Locale.US, "Live Movement: %+.2f in", currentPose.x()));
                    telemetry.addLine(String.format(Locale.US, "Raw Encoders : L:%d | R:%d | S:%d", rawL, rawR, rawS));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS: [A] Lock In | [B] Reset Step");

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.RIGHT_RESOLUTION);
                        statusWarning = "Step reset. Pose zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        double movement = currentPose.x();
                        if (Math.abs(movement) < 50.0) {
                            statusWarning = "Movement too small! Push robot before pressing [A].";
                            rumble(400);
                        } else {
                            rightTicksPerInch = Math.abs(movement / pushDistance);
                            rightEncoderDirection = (movement < 0 ? Encoder.REVERSE : Encoder.FORWARD);
                            rumble(150);
                            statusWarning = "";
                            enterStage(Stage.STRAFE_RESOLUTION);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 3: STRAFE ENCODER RESOLUTION & DIRECTION
                // -----------------------------------------------------------
                case STRAFE_RESOLUTION:
                    telemetry.addLine("  [STAGE 3/6: STRAFE POD RESOLUTION]");
                    telemetry.addLine("========================================");
                    telemetry.addLine(String.format(Locale.US, "ACTION: Push robot STRAIGHT LEFT exactly %.1f\"", pushDistance));
                    telemetry.addLine("Pedro Pathing coordinate (+Y = Left). Do not rotate.");
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine(String.format(Locale.US, "Live Movement: %+.2f in", currentPose.y()));
                    telemetry.addLine(String.format(Locale.US, "Raw Encoders : L:%d | R:%d | S:%d", rawL, rawR, rawS));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS: [A] Lock In | [B] Reset Step");

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.STRAFE_RESOLUTION);
                        statusWarning = "Step reset. Pose zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        double movement = currentPose.y();
                        if (Math.abs(movement) < 50.0) {
                            statusWarning = "Movement too small! Push robot left before pressing [A].";
                            rumble(400);
                        } else {
                            strafeTicksPerInch = Math.abs(movement / pushDistance);
                            strafeEncoderDirection = (movement < 0 ? Encoder.REVERSE : Encoder.FORWARD);

                            // Calculate forward and strafe scale factors
                            double forwardTicksPerInch = 2.0 / (1.0 / leftTicksPerInch + 1.0 / rightTicksPerInch);
                            forwardTicksToInches = 1.0 / forwardTicksPerInch;
                            strafeTicksToInches = 1.0 / strafeTicksPerInch;

                            rumble(150);
                            statusWarning = "";
                            enterStage(Stage.LEFT_OFFSET);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 4: LEFT POD OFFSET (Y)
                // -----------------------------------------------------------
                case LEFT_OFFSET:
                    telemetry.addLine("     [STAGE 4/6: LEFT POD OFFSET (Y)]");
                    telemetry.addLine("========================================");
                    telemetry.addLine("ACTION: Rotate robot 180° CCW about its center.");
                    telemetry.addLine("Pin center of robot in place! Use tile seam as guide.");
                    telemetry.addLine("----------------------------------------");
                    double estLeftY = -currentPose.x() / Math.PI;
                    telemetry.addLine(String.format(Locale.US, "Est. Left Pod Y : %+.4f in", estLeftY));
                    telemetry.addLine(String.format(Locale.US, "Displacement X  : %+.2f in", currentPose.x()));
                    telemetry.addLine(String.format(Locale.US, "Raw Encoders    : L:%d | R:%d | S:%d", rawL, rawR, rawS));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS: [A] Lock In | [B] Reset Step");

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.LEFT_OFFSET);
                        statusWarning = "Step reset. Pose zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        if (Math.abs(currentPose.x()) < 1.0) {
                            statusWarning = "Rotate 180° CCW before pressing [A]!";
                            rumble(400);
                        } else {
                            leftOffsets = List.of(-currentPose.x() / Math.PI, currentPose.y() / Math.PI);
                            rumble(150);
                            statusWarning = "";
                            enterStage(Stage.RIGHT_OFFSET);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 5: RIGHT POD OFFSET (Y)
                // -----------------------------------------------------------
                case RIGHT_OFFSET:
                    telemetry.addLine("    [STAGE 5/6: RIGHT POD OFFSET (Y)]");
                    telemetry.addLine("========================================");
                    telemetry.addLine("ACTION: Rotate robot 180° CCW about its center.");
                    telemetry.addLine("Pin center of robot in place! Use tile seam as guide.");
                    telemetry.addLine("----------------------------------------");
                    double estRightY = -currentPose.x() / Math.PI;
                    telemetry.addLine(String.format(Locale.US, "Est. Right Pod Y: %+.4f in", estRightY));
                    telemetry.addLine(String.format(Locale.US, "Displacement X  : %+.2f in", currentPose.x()));
                    telemetry.addLine(String.format(Locale.US, "Raw Encoders    : L:%d | R:%d | S:%d", rawL, rawR, rawS));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS: [A] Lock In | [B] Reset Step");

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.RIGHT_OFFSET);
                        statusWarning = "Step reset. Pose zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        if (Math.abs(currentPose.x()) < 1.0) {
                            statusWarning = "Rotate 180° CCW before pressing [A]!";
                            rumble(400);
                        } else {
                            rightOffsets = List.of(-currentPose.x() / Math.PI, currentPose.y() / Math.PI);
                            if (leftOffsets.get(0) <= rightOffsets.get(0)) {
                                statusWarning = "WARNING: leftPodY should be > rightPodY. Verify CCW rotation!";
                            }
                            rumble(150);
                            enterStage(Stage.TURN_MULTIPLIER);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 6: TURN MULTIPLIER IDENTIFICATION (MULTI-SPIN)
                // -----------------------------------------------------------
                case TURN_MULTIPLIER:
                    telemetry.addLine("      [STAGE 6/6: TURN MULTIPLIER]");
                    telemetry.addLine("========================================");
                    telemetry.addLine(String.format(Locale.US, "ACTION: Rotate robot %d FULL TURN%s CCW (%.0f° total)",
                            targetSpins, targetSpins > 1 ? "S" : "", targetSpins * 360.0));
                    telemetry.addLine("Keep robot center in place. Use tape mark for reference.");
                    telemetry.addLine("----------------------------------------");

                    double heading = localizer.getTotalHeading();
                    double deltaHeading = heading - startHeading;
                    double currentDeg = Math.toDegrees(deltaHeading);
                    double targetDeg = targetSpins * 360.0;
                    double progress = Math.min(100.0, Math.max(0.0, (currentDeg / targetDeg) * 100.0));

                    telemetry.addLine(String.format(Locale.US, "Turn Angle : %+.1f° / %.0f°", currentDeg, targetDeg));
                    telemetry.addLine(String.format(Locale.US, "Rotations  : %.2f / %d turns", currentDeg / 360.0, targetSpins));
                    telemetry.addLine(progressBar(progress));
                    displayWarning();
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("D-Pad UP/DN: Adjust target spin count");
                    telemetry.addLine("CONTROLS   : [A] Lock In & Finish | [B] Reset Step");

                    // Allow adjusting target turns on this step if needed
                    if (gamepad1.dpadUpWasPressed()) targetSpins++;
                    if (gamepad1.dpadDownWasPressed() && targetSpins > 1) targetSpins--;

                    if (gamepad1.bWasPressed()) {
                        enterStage(Stage.TURN_MULTIPLIER);
                        statusWarning = "Step reset. Heading zeroed.";
                    }

                    if (gamepad1.aWasPressed()) {
                        if (deltaHeading <= 1.0) {
                            statusWarning = "Inadequate rotation! Rotate CCW before pressing [A].";
                            rumble(400);
                        } else {
                            // Scaled for targetSpins revolutions (targetAngle = targetSpins * 2 * PI)
                            turnTicksToRadians = currentConfig.turnTicksToRadians.get() * (targetSpins * 2.0 * Math.PI) / deltaHeading;
                            strafePodX = (leftOffsets.get(1) + rightOffsets.get(1)) / 2.0 * turnTicksToRadians / strafeTicksToInches;

                            logFinalResults();
                            rumble(600);
                            enterStage(Stage.COMPLETE);
                        }
                    }
                    break;

                // -----------------------------------------------------------
                // STAGE 7: COMPLETE & DISPLAY
                // -----------------------------------------------------------
                case COMPLETE:
                    telemetry.addLine("         *** TUNING COMPLETE! ***");
                    telemetry.addLine("========================================");
                    telemetry.addLine("// Values logged to RobotLog (logcat):");
                    telemetry.addLine(String.format(Locale.US, "c.leftPodY.set(%.4f);", leftOffsets.get(0)));
                    telemetry.addLine(String.format(Locale.US, "c.rightPodY.set(%.4f);", rightOffsets.get(0)));
                    telemetry.addLine(String.format(Locale.US, "c.strafePodX.set(%.4f);", strafePodX));
                    telemetry.addLine(String.format(Locale.US, "c.forwardTicksToInches.set(%.14f);", forwardTicksToInches));
                    telemetry.addLine(String.format(Locale.US, "c.strafeTicksToInches.set(%.14f);", strafeTicksToInches));
                    telemetry.addLine(String.format(Locale.US, "c.turnTicksToRadians.set(%.16f);", turnTicksToRadians));
                    telemetry.addLine("c.leftEncoderDirection.set(" + direction(leftEncoderDirection) + ");");
                    telemetry.addLine("c.rightEncoderDirection.set(" + direction(rightEncoderDirection) + ");");
                    telemetry.addLine("c.strafeEncoderDirection.set(" + direction(strafeEncoderDirection) + ");");
                    telemetry.addLine("----------------------------------------");
                    telemetry.addLine("CONTROLS:");
                    telemetry.addLine("  [X] -> RESTART TUNING FROM BEGINNING");

                    if (gamepad1.xWasPressed()) {
                        rumble(200);
                        enterStage(Stage.SETUP);
                    }
                    break;
            }

            telemetry.addLine("========================================");
            telemetry.update();
        }
    }

    private void enterStage(Stage stage) {
        currentStage = stage;

        switch (stage) {
            case LEFT_RESOLUTION:
                currentConfig = config(true, 1.0, 1.0, Encoder.FORWARD, Encoder.FORWARD, Encoder.FORWARD);
                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                break;

            case RIGHT_RESOLUTION:
                currentConfig = config(false, 1.0, 1.0, Encoder.FORWARD, Encoder.FORWARD, Encoder.FORWARD);
                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                break;

            case STRAFE_RESOLUTION:
                currentConfig = config(true, 1.0, 1.0, Encoder.FORWARD, Encoder.FORWARD, Encoder.FORWARD);
                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                break;

            case LEFT_OFFSET:
                currentConfig = config(true, forwardTicksToInches, strafeTicksToInches,
                        leftEncoderDirection, rightEncoderDirection, strafeEncoderDirection);
                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                localizer.update();
                break;

            case RIGHT_OFFSET:
                currentConfig = config(false, forwardTicksToInches, strafeTicksToInches,
                        leftEncoderDirection, rightEncoderDirection, strafeEncoderDirection);
                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                localizer.update();
                break;

            case TURN_MULTIPLIER:
                currentConfig = config(true, forwardTicksToInches, strafeTicksToInches,
                        leftEncoderDirection, rightEncoderDirection, strafeEncoderDirection);
                currentConfig.leftPodY.set(leftOffsets.get(0));
                currentConfig.rightPodY.set(rightOffsets.get(0));
                currentConfig.turnTicksToRadians.set(forwardTicksToInches);

                localizer = new ThreeWheelLocalizer(hardwareMap, currentConfig);
                localizer.setPose(new Pose(0, 0));
                localizer.update();
                startHeading = localizer.getTotalHeading();
                break;

            case SETUP:
            case COMPLETE:
            default:
                break;
        }
    }

    private ThreeWheelConfig config(boolean left, double forward, double strafe,
                                    double leftDir, double rightDir, double strafeDir) {
        return new ThreeWheelConfig(c -> {
            c.leftEncoderName.set(leftEncoderName);
            c.rightEncoderName.set(rightEncoderName);
            c.strafeEncoderName.set(strafeEncoderName);
            c.leftPodY.set(left ? 0.0 : 1.0);
            c.rightPodY.set(left ? -1.0 : 0.0);
            c.strafePodX.set(0.0);
            c.forwardTicksToInches.set(forward);
            c.strafeTicksToInches.set(strafe);
            c.turnTicksToRadians.set(0.0);
            c.leftEncoderDirection.set(leftDir);
            c.rightEncoderDirection.set(rightDir);
            c.strafeEncoderDirection.set(strafeDir);
        });
    }

    private void initHardware() {
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        for (String name : new String[]{frontLeftName, backLeftName, frontRightName, backRightName}) {
            DcMotorEx motor = hardwareMap.tryGet(DcMotorEx.class, name);
            if (motor != null) {
                motor.setPower(0);
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
        }

        leftMotor = hardwareMap.tryGet(DcMotorEx.class, leftEncoderName);
        rightMotor = hardwareMap.tryGet(DcMotorEx.class, rightEncoderName);
        strafeMotor = hardwareMap.tryGet(DcMotorEx.class, strafeEncoderName);
    }

    private static String direction(double dir) {
        return dir == Encoder.REVERSE ? "Encoder.REVERSE" : "Encoder.FORWARD";
    }

    private void rumble(int ms) {
        try {
            gamepad1.rumble(ms);
        } catch (Exception ignored) {}
    }

    private void displayWarning() {
        if (!statusWarning.isEmpty()) {
            telemetry.addLine("----------------------------------------");
            telemetry.addLine(">> " + statusWarning);
        }
    }

    private String progressBar(double pct) {
        int totalBars = 20;
        int filled = (int) Math.round((pct / 100.0) * totalBars);
        filled = Math.max(0, Math.min(totalBars, filled));
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < filled; i++) sb.append("=");
        for (int i = filled; i < totalBars; i++) sb.append(" ");
        sb.append(String.format(Locale.US, "] %.0f%%", pct));
        return sb.toString();
    }

    private void logFinalResults() {
        String logOutput = "\n" +
                "================== PEDRO PATHING THREE-WHEEL CONFIG ==================\n" +
                "public static ThreeWheelConfig localizerConfig = new ThreeWheelConfig(c -> {\n" +
                "    c.leftEncoderName.set(Settings.LOCALIZATION.NAMES.LEFT);\n" +
                "    c.rightEncoderName.set(Settings.LOCALIZATION.NAMES.RIGHT);\n" +
                "    c.strafeEncoderName.set(Settings.LOCALIZATION.NAMES.STRAFE);\n" +
                String.format(Locale.US, "    c.leftPodY.set(%.4f);\n", leftOffsets.get(0)) +
                String.format(Locale.US, "    c.rightPodY.set(%.4f);\n", rightOffsets.get(0)) +
                String.format(Locale.US, "    c.strafePodX.set(%.4f);\n", strafePodX) +
                String.format(Locale.US, "    c.forwardTicksToInches.set(%.14f);\n", forwardTicksToInches) +
                String.format(Locale.US, "    c.strafeTicksToInches.set(%.14f);\n", strafeTicksToInches) +
                String.format(Locale.US, "    c.turnTicksToRadians.set(%.16f);\n", turnTicksToRadians) +
                "    c.leftEncoderDirection.set(" + direction(leftEncoderDirection) + ");\n" +
                "    c.rightEncoderDirection.set(" + direction(rightEncoderDirection) + ");\n" +
                "    c.strafeEncoderDirection.set(" + direction(strafeEncoderDirection) + ");\n" +
                "});\n" +
                "======================================================================\n";

        RobotLog.i("PedroThreeWheelTuner", logOutput);
    }
}