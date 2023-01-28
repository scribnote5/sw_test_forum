// autoComplete
import autoComplete from '@tarekraafat/autocomplete.js'

let autoCompleteData = [];

const createAutoComplete = (dataValue, inputName, ref) => {
    new autoComplete({
        selector: 'input[name="' + inputName + '"]',
        data: {
            src: dataValue,
            cache: false,
        },
        resultItem: {
            highlight: {
                render: true
            }
        },
        events: {
            input: {
                selection: (event) => {
                    ref.value = event.detail.selection.value;
                },
            },
        }
    });
}

const createHashTagsAutoComplete = (dataValue, inputName) => {
    new autoComplete({
        selector: 'input[name="' + inputName + '"]',
        data: {
            src: dataValue,
            cache: false,
        },
        resultItem: {
            highlight: {
                render: true
            }
        },
        events: {
            input: {
                selection: (event) => {
                    document.getElementsByName(inputName)[0].value = event.detail.selection.value;
                },
            },
        }
    });
}

export {createAutoComplete, createHashTagsAutoComplete};