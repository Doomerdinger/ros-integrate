package ros.integrate.pkt.psi;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ros.integrate.pkt.file.ROSMsgFileType;

/**
 * a ROS message, a one-directional message sent between (and within) executables.
 */
public class ROSMsgFile extends ROSPktFile {
    public ROSMsgFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ROSMsgFileType.INSTANCE;
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return null;
    }

    @Override
    String getDotDefaultExtension() {
        return ROSMsgFileType.DOT_DEFAULT_EXTENSION;
    }

    @Override
    public int getMaxSeparators() {
        return 0;
    }

    @Override
    public String getTooManySeparatorsMessage() {
        return "ROS Messages cannot have separators";
    }
}
