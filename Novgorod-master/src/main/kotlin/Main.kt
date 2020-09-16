import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import javafx.stage.Stage
import tornadofx.App

class Main: App(LogIn::class) {
    override val primaryView = LogIn::class

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}