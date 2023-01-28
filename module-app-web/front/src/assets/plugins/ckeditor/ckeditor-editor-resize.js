import Plugin from '@ckeditor/ckeditor5-core/src/plugin';
import ButtonView from '@ckeditor/ckeditor5-ui/src/button/buttonview';

// import extendIcon from '/public/extend-black.svg';
// import shrinkIcon from '/public/shrink-black.svg';
// import extendIcon from '@/assets/images/shrink-black.svg';
// import shrinkIcon from '@/assets/images/shrink-black.svg';
import extendIcon from '@ckeditor/ckeditor5-core/theme/icons/extend-black.svg';
import shrinkIcon from '@ckeditor/ckeditor5-core/theme/icons/shrink-black.svg';

// custom editor resize pluginckeidt
class EditorResize extends Plugin {
    init() {
        const editor = this.editor;

        editor.ui.componentFactory.add('editorResize', locale => {
            const view = new ButtonView(locale);
            let isFullSize = false;

            view.set({
                label: 'Extend editor',
                icon: extendIcon,
                tooltip: true
            });

            // Callback executed once the image is clicked.
            view.on('execute', () => {
                editor.model.change(writer => {
                    if (isFullSize) {
                        document.styleSheets[0].deleteRule(0);
                        document.styleSheets[0].deleteRule(1);
                        document.styleSheets[0].deleteRule(2);

                        document.styleSheets[0].insertRule(
                            ".ck-editor__editable:not(.ck-editor__nested-editable) { " +
                            "height: 550px; }", 0);

                        view.set({
                            label: 'Extend editor',
                            icon: extendIcon,
                            tooltip: true
                        });
                        isFullSize = false;
                        editor.editing.view.focus()
                    } else {
                        document.styleSheets[0].deleteRule(0);

                        document.styleSheets[0].insertRule(
                            ".ck.ck-editor { " +
                            "position: fixed !important; " +
                            "top: 0px !important; " +
                            "left: 0px !important; " +
                            "z-index: 1200 !important; " +
                            "width:" + window.innerWidth + "px !important; }", 0);
                        document.styleSheets[0].insertRule(
                            ".ck-editor__editable:not(.ck-editor__nested-editable) { " +
                            "height: calc(" + window.innerHeight + "px - 77px) !important; }", 1);
                        document.styleSheets[0].insertRule(
                            "body { overflow-y: hidden !important }", 2);

                        view.set({
                            label: 'Shrink editor',
                            icon: shrinkIcon,
                            tooltip: true
                        });
                        isFullSize = true;
                        editor.editing.view.focus()
                    }
                });
            });

            return view;
        });
    }
}

export {EditorResize}