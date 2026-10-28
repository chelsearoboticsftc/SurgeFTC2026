package org.firstinspires.ftc.teamcode.Autos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
@Autonomous
public class EightShootAutoBlue extends OpMode{

private Follower follower;
private shooter shooter;

        private final PoseFactory poseFactory = PoseFactory.degrees();

        private final Pose start = poseFactory.of(84, 132, 90);
        private final Pose path1 = poseFactory.of(84, 126, 0);
        private final Pose point2Start = poseFactory.of(84, 126, 0);
        private final Pose point2 = poseFactory.of(114, 132, 0);
        private final Pose point3Start = poseFactory.of(114, 132, 0);
        private final Pose point3 = poseFactory.of(132, 132, 0);
        private final Pose point4Start = poseFactory.of(132, 132, 0);
        private final Pose point4 = poseFactory.of(96, 21, 0);
        private final Pose point5Start = poseFactory.of(96, 21, 0);
        private final Pose point5 = poseFactory.of(84, 18, 270);
        private final Pose point6Start = poseFactory.of(84, 18, 270);
        private final Pose point6 = poseFactory.of(132, 36, 270);

        public Path path1() {
            return Paths.line(start, path1).linear(start, path1);
        }

        public Path path2() {
            return Paths.line(point2Start, point2).linear(point2Start, point2);
        }

        public Path path3() {
            return Paths.line(point3Start, point3).linear(point3Start, point3);
        }

        public Path path4() {
            return Paths.line(point4Start, point4).linear(point4Start, point4);
        }

        public Path path5() {
            return Paths.line(point5Start, point5).linear(point5Start, point5);
        }

        public Path path6() {
            return Paths.line(point6Start, point6).linear(point6Start, point6);

            }
    private CommandBuilder autoRoutine() {
            return sequential(
                    follow(follower,path1()),
                    follow(follower,path2()),
                    follow(follower,path3()),
                    follow(follower,path4()),
                    follow(follower,path5()),
                    follow(follower,path6())
            );
    }
    @Override
    public void start(){schedule(autoRoutine());}
    @Override
    public void loop() {
            follower.update();
        Scheduler.execute();
    }
    @Override
    public void stop() {
            OpModeStorage.autonomousEndPose = follower.pose();
    }

    }

