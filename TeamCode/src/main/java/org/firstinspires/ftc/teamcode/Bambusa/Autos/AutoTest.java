package org.firstinspires.ftc.teamcode.Bambusa.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Conveyor;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Launcher;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Auto Test")
public class AutoTest extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(31.9446, 8.3887, 90);
    private final Pose path1 = poseFactory.of(54.2382, 113.8834, 180);
    private final Pose path1Control1 = poseFactory.of(10.2327, 123.9175, 0);
    private final Pose launch = poseFactory.of(123.1444, 108.6022, 175.6172);
    private final Pose point3 = poseFactory.of(74.1407, 93.2876, -107.7464);
    private final Pose point3Control1 = poseFactory.of(141.5, 9.2383, 0);
    private final Pose point3Control2 = poseFactory.of(45.1122, 2.5829, 0);

    Launcher launcher;
    Conveyor conveyor;

    // Autonomous routine scheduled sequentially
    public Command autoRoutine() {
        return sequential(
                // Enable both subsystems at start of autonomous
                launcher.enableCommand(),
                conveyor.enableCommand(),

                follow(follower, path1()),
                follow(follower, launch()),
                // Trigger shooting command after launch path
                conveyor.takeoutCommand(follower),
                follow(follower, path3())
        );
    }

    @Override
    public void runOpMode() {
        // Initialize subsystems with motors off during init
        launcher = new Launcher(hardwareMap);
        conveyor = new Conveyor(hardwareMap);

        launcher.init(telemetry);
        conveyor.init(telemetry);

        // Initialize follower
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        Scheduler.reset();

        waitForStart();

        // Schedule full auto command sequence
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }

        // Stop all motors after autonomous period ends
        launcher.stop();
        conveyor.stop();
        Scheduler.reset();
    }

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    public Path launch() {
        return Paths.line(path1, launch).reverseTangent();
    }

    public Path path3() {
        return Paths.curve(launch, point3Control1, point3Control2, point3).reverseTangent();
    }
}