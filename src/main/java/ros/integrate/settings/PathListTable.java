package ros.integrate.settings;

import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.ComponentWithBrowseButton.BrowseFolderActionListener;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.UIBundle;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PathListTable extends ListTableWithButtons<PathListTable.Path> {
    static class Path {
        private String path;

        String get() {
            return path;
        }

        void set(@NotNull String path) {
            this.path = path;
        }

        @Contract(pure = true)
        Path(@NotNull String path) {
            this.path = path;
        }
    }

    private final BrowserOptions browserOptions;

    PathListTable(BrowserOptions browserOptions) {
        getTableView().getEmptyText().setText("No paths specified");
        this.browserOptions = browserOptions;
    }

    @Override
    protected ListTableModel createListModel() {
        return new ListTableModel(new ElementsColumnInfoBase<Path>("") {
            @Contract("null -> !null")
            @Nullable
            @Override
            public String valueOf(Path path) {
                return path == null ? "" : path.get();
            }

            @Contract("null -> !null")
            @Nullable
            @Override
            protected String getDescription(Path path) {
                return valueOf(path);
            }

            @Override
            public void setValue(Path oldPath, @NotNull String value) {
                if (value.equals(valueOf(oldPath))) {
                    return;
                }
                oldPath.set(value);
                setModified();
            }

            @Override
            public boolean isCellEditable(Path path) {
                return canDeleteElement(path);
            }
        });
    }

    @Override
    protected Path createElement() {
        return new Path("");
    }

    @Override
    protected boolean isEmpty(@NotNull Path element) {
        return element.get().isEmpty();
    }

    @Override
    protected Path cloneElement(@NotNull Path pathToClone) {
        return new Path(pathToClone.get());
    }

    @Override
    protected boolean canDeleteElement(Path selection) {
        return true;
    }

    List<String> getPaths() {
        return getElements().stream().map(Path::get).collect(Collectors.toList());
    }

    void setValues(@NotNull Collection<String> paths) {
        setValues(paths.stream().map(Path::new).collect(Collectors.toList()));
    }

    @NotNull
    @Override
    protected AnActionButton[] createExtraActions() {
        AnActionButton duplicateButton = new AnActionButton("Duplicate Path", AllIcons.Actions.Copy) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                stopEditing();
                getSelection().forEach(path -> addNewElement(cloneElement(path)));
            }

            @Override
            public boolean isEnabled() {
                return !getSelection().isEmpty();
            }
        };
        if (browserOptions.addBrowser) {
            AnActionButton browseButton = new AnActionButton(UIBundle.message("component.with.browse.button.browse.button.tooltip.text"), AllIcons.Actions.Menu_open) {
                private final TextFieldWithBrowseButton dummy = new TextFieldWithBrowseButton();
                private final BrowseFolderActionListener action =
                        new BrowseFolderActionListener<>(browserOptions.title,
                                browserOptions.description, dummy, browserOptions.project,
                                FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    stopEditing();
                    action.run();
                    getSelection().forEach(path -> path.set(dummy.getText()));
                }

                @Override
                public boolean isEnabled() {
                    return getSelection().size() == 1;
                }
            };
            return new AnActionButton[]{duplicateButton, browseButton};
        } else {
            return new AnActionButton[]{duplicateButton};
        }
    }
}
