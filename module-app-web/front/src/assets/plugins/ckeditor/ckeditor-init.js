import Essentials from "@ckeditor/ckeditor5-essentials/src/essentials";
import Bold from "@ckeditor/ckeditor5-basic-styles/src/bold";
import Italic from "@ckeditor/ckeditor5-basic-styles/src/italic";
import Code from "@ckeditor/ckeditor5-basic-styles/src/code";
import Strikethrough from "@ckeditor/ckeditor5-basic-styles/src/strikethrough";
import Subscript from "@ckeditor/ckeditor5-basic-styles/src/subscript";
import Superscript from "@ckeditor/ckeditor5-basic-styles/src/superscript";
import Underline from "@ckeditor/ckeditor5-basic-styles/src/underline";
import Image from "@ckeditor/ckeditor5-image/src/image";
import ImageCaption from "@ckeditor/ckeditor5-image/src/imagecaption";
import ImageStyle from "@ckeditor/ckeditor5-image/src/imagestyle";
import ImageToolbar from "@ckeditor/ckeditor5-image/src/imagetoolbar";
import ImageResize from "@ckeditor/ckeditor5-image/src/imageresize";
import ImageInsert from "@ckeditor/ckeditor5-image/src/imageinsert";
import ImageUpload from "@ckeditor/ckeditor5-image/src/imageupload";
import AutoImage from "@ckeditor/ckeditor5-image/src/autoimage";
import SpecialCharacters from "@ckeditor/ckeditor5-special-characters/src/specialcharacters";
import SpecialCharactersCurrency from "@ckeditor/ckeditor5-special-characters/src/specialcharacterscurrency";
import SpecialCharactersLatin from "@ckeditor/ckeditor5-special-characters/src/specialcharacterslatin";
import SpecialCharactersMathematical from "@ckeditor/ckeditor5-special-characters/src/specialcharactersmathematical";
import FontBackgroundColor from "@ckeditor/ckeditor5-font/src/fontbackgroundcolor";
import FontFamily from "@ckeditor/ckeditor5-font/src/fontfamily";
import FontSize from "@ckeditor/ckeditor5-font/src/fontsize";
import FontColor from "@ckeditor/ckeditor5-font/src/fontcolor";
import FindAndReplace from '@ckeditor/ckeditor5-find-and-replace/src/findandreplace';
import Link from "@ckeditor/ckeditor5-link/src/link";
import AutoLink from "@ckeditor/ckeditor5-link/src/autolink";
import Table from "@ckeditor/ckeditor5-table/src/table";
import TableToolbar from "@ckeditor/ckeditor5-table/src/tabletoolbar";
import TableProperties from "@ckeditor/ckeditor5-table/src/tableproperties";
import TableCellProperties from "@ckeditor/ckeditor5-table/src/tablecellproperties";
import Heading from "@ckeditor/ckeditor5-heading/src/heading";
import Indent from "@ckeditor/ckeditor5-indent/src/indent";
import IndentBlock from "@ckeditor/ckeditor5-indent/src/indentblock";
import MediaEmbed from "@ckeditor/ckeditor5-media-embed/src/mediaembed";
import List from "@ckeditor/ckeditor5-list/src/list";
import ListProperties from "@ckeditor/ckeditor5-list/src/listproperties";
import BlockQuote from "@ckeditor/ckeditor5-block-quote/src/blockquote";
import RemoveFormat from "@ckeditor/ckeditor5-remove-format/src/removeformat";
import PasteFromOffice from "@ckeditor/ckeditor5-paste-from-office/src/pastefromoffice";
import CKFinderUploadAdapter from "@ckeditor/ckeditor5-adapter-ckfinder/src/uploadadapter";
import UploadAdapter from "@ckeditor/ckeditor5-adapter-ckfinder/src/uploadadapter";
import CodeBlock from "@ckeditor/ckeditor5-code-block/src/codeblock";
import Highlight from "@ckeditor/ckeditor5-highlight/src/highlight";
import HorizontalLine from "@ckeditor/ckeditor5-horizontal-line/src/horizontalline";
import Paragraph from "@ckeditor/ckeditor5-paragraph/src/paragraph";
import Alignment from "@ckeditor/ckeditor5-alignment/src/alignment";
import HtmlEmbed from "@ckeditor/ckeditor5-html-embed/src/htmlembed";
import {Mention} from "@ckeditor/ckeditor5-mention";
import SourceEditing from "@ckeditor/ckeditor5-source-editing/src/sourceediting";
import {CustomUploadAdapterPlugin} from "@/assets/plugins/ckeditor/ckeditor-upload-adapter";
import ClassicEditor from "@ckeditor/ckeditor5-editor-classic/src/classiceditor";

import {EditorResize} from "./ckeditor-editor-resize"

const editor = ClassicEditor;

const editorGeneralData = '';

const editorRuleData =
    '<h5>' +
    '    [규칙 요약(위배 원인 -> 수정 방법)]' +
    '</h5>' +
    '<p>' +
    '   예제) Unreachable code는 불필요한 메모리 사용 및 명확한 개발 의도를 전달 할 수 없음' +
    '</p>' +
    '<p>' +
    '   -> 프로그램 문법적으로 도달할 수 없는 코드는 개발 의도를 재확인하고 제거' +
    '</p>' +
    '<hr>' +
    '<h5>' +
    '    [위험 요인]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [예외 사항]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [상세 설명]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [원문]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>';

let editorExampleData =
    '<h5>' +
    '    [예제 요약(위배 원인 -> 수정 방법)]' +
    '</h5>' +
    '<p>' +
    '   예제) 함수에서 사용하지 않는 매개변수 선언' +
    '</p>' +
    '<p>' +
    '   -> 함수에서 사용하지 않는 매개변수 제거' +
    '</p>' +
    '<hr>' +
    '<h5>' +
    '    [상세 설명]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>';

let editorGuidelineData =
    '<h5>' +
    '    [가이드라인 요약(위배 원인 -> 수정 방법)]' +
    '</h5>' +
    '<p>' +
    '   예제) 함수에서 사용하지 않는 매개변수 선언' +
    '</p>' +
    '<p>' +
    '   -> 함수에서 사용하지 않는 매개변수 제거' +
    '</p>' +
    '<hr>' +
    '<h5>' +
    '    [상세 설명]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>';

let editorToolConfigurationData =
    '<h5>' +
    '    [부가 환경 정보]' +
    '</h5>' +
    '<p>' +
    '    위에서 작성하지 못한 환경 정보를 작성해주세요.' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [관련 트러블슈팅]' +
    '</h5>' +
    '<p>' +
    '    예제) tool-trouble-shooting/read/2' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [지원 이력]' +
    '</h5>' +
    '<p>' +
    '    예제) <a href="http://support.suresofttech.com/ko/support/tickets/10220">http://support.suresofttech.com/ko/support/tickets/10220</a>' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>'

let editorToolTroubleShootingData =
    '<h5>' +
    '    [오류 상황]' +
    '</h5>' +
    '<p>' +
    '    무엇을 하다가 오류가 발생 하였는지, 어떤 기능이 안되는지 등을 작성하세요.' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [오류 로그]' +
    '</h5>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '                #파일 경로 및 이름(자동 완성 제공)' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '                -' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [발생 원인]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [해결 방법]' +
    '</h5>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '                #파일 경로 및 이름(자동 완성 제공)' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '                로그 -' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [지원 이력]' +
    '</h5>' +
    '<p>' +
    '    예제)  <a href="http://support.suresofttech.com/ko/support/tickets/10220">http://support.suresofttech.com/ko/support/tickets/10220</a>' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>';

let editorToolUsageMethodData =
    '<h5>' +
    '    [상세 설명]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>';

let editorDynamicTestData =
    '<h5>' +
    '    [가이드라인 요약(원인 -> 대응 방법)]' +
    '</h5>' +
    '<p>' +
    '   예제) 탐침 코드로 실시간성 저하로, 인터럽트 함수가 호출하지 않음' +
    '</p>' +
    '<p>' +
    '   -> 도구 개발사에서 고객 지원 확인서 발급' +
    '</p>' +
    '<hr>' +
    '<h5>' +
    '    [상세 설명]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    [비고]' +
    '</h5>' +
    '<p>' +
    '    -' +
    '</p>';

let editorFinalTestData =
    '<h5>' +
    '    1. 과제 개요' +
    '</h5>' +
    '<p>' +
    '    - 과제명: ' +
    '</p>' +
    '<p>' +
    '    - 기간: ' +
    '</p>' +
    '<p>' +
    '    - 시제 업체: ' +
    '</p>' +
    '<p>' +
    '    - 컴파일러: ' +
    '</p>' +
    '<p>' +
    '    - 신뢰성시험 도구 정보: ' +
    '</p>' +
    '<p>' +
    '    - 비고: ' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '     2. 실사 개요' +
    '</h5>' +
    '<p>' +
    '    - 기간: ' +
    '</p>' +
    '<p>' +
    '    - 장소: ' +
    '</p>' +
    '<p>' +
    '    - 실사 담당자: ' +
    '</p>' +
    '<p>' +
    '    - 비고: ' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    3. 정적시험 개요' +
    '</h5>' +
    '<p>' +
    '    - 정적시험 규칙 정보: ' +
    '</p>' +
    '<p>' +
    '    - 정적시험 이슈 사항에 대하여 작성 해주세요. ' +
    '</p>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   사전 제외, 예외 처리, 문서화 규칙) 규칙 정보를 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 사전 제외) [MISRA_C_2012_01_02] - 언어 확장 사용 금지' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '                사유를 작성하세요.' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    4. 동적시험 개요' +
    '</h5>' +
    '<p>' +
    '    - 동적시험 이슈 사항에 대하여 작성 해주세요. ' +
    '</p>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   도구 미지원, 사전 제외, 예외 처리) 이슈 제목을 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 도구 미지원) 람다식 사용' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   사유를 작성하세요.' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    5. 특이사항' +
    '</h5>' +
    '<p>' +
    '    - 기타 특이사항이 발생한 부분에 대하여 작성 해주세요. ' +
    '</p>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   실사 담당관 문의 사항, 실사 담당관 요청 사항, 문의 사항, 요청 사항, 기타 사항) 및 제목을 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 문의 사항) 현재 사용하는 정적시험 도구를 추후 변경하면, 추가로 발생하는 위배에 대한 지원 확인서가 발급되는가?' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   답변 및 대응 -' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   실사 담당관 문의 사항, 실사 담당관 요청 사항, 문의 사항, 요청 사항, 기타 사항) 및 제목을 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 요청 사항) 기존 소스 코드에서 변경된 코드에 대해서만 신뢰성시험을 요청.' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   답변 및 대응 -' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '     6. 업로드 자료 개요' +
    '</h5>' +
    '<p>' +
    '    - 업로드 자료 종류: 예제) 오탐 문의 사례, 신뢰성시험 보고서' +
    '</p>' +
    '<p>' +
    '    - 비고: ' +
    '</p>'

let editorEducationData =
    '<h5>' +
    '    1. 교육 개요' +
    '</h5>' +
    '<p>' +
    '    - 과제명: ' +
    '</p>' +
    '<p>' +
    '    - 대상 업체: ' +
    '</p>' +
    '<p>' +
    '    - 비고: ' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    2. 특이사항' +
    '</h5>' +
    '<p>' +
    '    - 기타 특이사항이 발생한 부분에 대하여 작성 해주세요. ' +
    '</p>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '               문의 사항, 요청 사항) 및 제목을 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 문의 사항) C# 취약점 점검 규칙 문의' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   답변 및 대응 -' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>';

let editorReportData =
    '<h5>' +
    '    1. 과제 개요' +
    '</h5>' +
    '<p>' +
    '    - 과제명: ' +
    '</p>' +
    '<p>' +
    '    - 기간: ' +
    '</p>' +
    '<p>' +
    '    - 시제 업체: ' +
    '</p>' +
    '<p>' +
    '    - 비고: ' +
    '</p>' +
    '<p>' +
    '    &nbsp;' +
    '</p>' +
    '<h5>' +
    '    2. 특이사항' +
    '</h5>' +
    '<p>' +
    '    - 기타 특이사항이 발생한 부분에 대하여 작성 해주세요. ' +
    '</p>' +
    '<figure class="table">' +
    '    <table>' +
    '        <tbody>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '               문의 사항, 요청 사항) 및 제목을 작성하세요.' +
    '               </p>' +
    '               <p>' +
    '                   예제) 문의 사항) C# 취약점 점검 규칙 문의' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        <tr>' +
    '            <td>' +
    '               <p>' +
    '                   답변 및 대응 -' +
    '               </p>' +
    '            </td>' +
    '        </tr>' +
    '        </tbody>' +
    '    </table>' +
    '</figure>';

let editorConfig = {
    plugins: [
        Essentials,
        Bold,
        Italic,
        Code,
        Strikethrough,
        Subscript,
        Superscript,
        Underline,

        Image,
        ImageCaption,
        ImageStyle,
        ImageToolbar,
        ImageResize,
        ImageInsert,
        ImageUpload,
        AutoImage,

        SpecialCharacters,
        SpecialCharactersCurrency,
        SpecialCharactersLatin,
        SpecialCharactersMathematical,

        FontBackgroundColor,
        FontFamily,
        FontSize,
        FontColor,
        FindAndReplace,

        Link,
        AutoLink,

        Table,
        TableToolbar,
        TableProperties,
        TableCellProperties,

        Heading,
        // Title,

        Indent,
        IndentBlock,

        MediaEmbed,

        List,
        ListProperties,

        BlockQuote,
        RemoveFormat,
        PasteFromOffice,
        // TextTransformation,
        CKFinderUploadAdapter,
        CodeBlock,
        Highlight,
        HorizontalLine,
        Paragraph,
        Alignment,
        HtmlEmbed,
        UploadAdapter,
        //EasyImage,
        //CloudServices,
        Mention,
        // MentionCustomization,
        SourceEditing,

        EditorResize
    ],

    toolbar: {
        items: [
            'heading',
            '|',
            'alignment',
            '|',
            'fontFamily',
            'fontSize',
            'fontColor',
            'fontBackgroundColor',
            'highlight',
            '|',
            'blockQuote',
            'bold',
            'italic',
            'underline',
            'strikethrough',
            'removeFormat',
            '-',
            'bulletedList',
            'numberedList',
            '|',
            'outdent',
            'indent',
            '|',
            'findAndReplace',
            'imageInsert',
            'insertTable',
            '|',
            'link',
            'horizontalLine',
            'specialCharacters',
            'superscript',
            'subscript',
            '|',
            'mediaEmbed',
            'codeBlock',
            'htmlEmbed',
            'code',
            '|',
            'undo',
            'redo',
            '|',
            'sourceEditing',
            '|',
            'editorResize'
        ],
        shouldNotGroupWhenFull: true
    },

    codeBlock: {
        languages: [
            {language: 'plaintext', label: 'Plain text', class: ''},
            {language: 'c', label: 'C'},
            {language: 'cpp', label: 'C++'},
            {language: 'java', label: 'Java'},
            {language: 'cs', label: 'C#'},
        ]
    },

    heading: {
        options: [
            {model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph'},
            {model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1'},
            {model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2'},
            {model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3'},
            {model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4'},
            {model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5'},
            {model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6'},
        ]
    },

    image: {
        // Configure the available styles.
        styles: [
            'alignLeft', 'alignCenter', 'alignRight'
        ],

        // Configure the available image resize options.
        resizeUnit: 'px',
        resizeOptions: [
            {
                name: 'resizeImage:original',
                label: 'Original',
                value: null
            },
            {
                name: 'resizeImage:500',
                label: '500px',
                value: '500'
            },
            {
                name: 'resizeImage:250',
                label: '250px',
                value: '250'
            },
            {
                name: 'resizeImage:100',
                label: '100px',
                value: '100'
            },
            {
                name: 'resizeImage:50',
                label: '50px',
                value: '50'
            },
            {
                name: 'resizeImage:25',
                label: '25px',
                value: '25'
            }
        ],

        // You need to configure the image toolbar, too, so it shows the new style
        // buttons as well as the resize buttons.
        toolbar: [
            'imageStyle:alignLeft', 'imageStyle:alignCenter', 'imageStyle:alignRight',
            '|',
            'imageResize',
            '|',
            'imageTextAlternative'
        ]
    },

    table: {
        contentToolbar: [
            'tableColumn',
            'tableRow',
            'mergeTableCells',
            'tableCellProperties',
            'tableProperties'
        ]
    },

    mention: {
        feeds: [
            {
                marker: '#',
                feed: getFeedItems,
                itemRenderer: customItemRenderer
            }
        ]
    },

    extraPlugins: [CustomUploadAdapterPlugin]
};

function getFeedItems(queryText) {
    // As an example of an asynchronous action, return a promise
    // that resolves after a 100ms timeout.
    // This can be a server request or any sort of delayed action.
    return new Promise(resolve => {
        setTimeout(() => {
            const itemsToDisplay = mentionItems
                // Filter out the full list of all items to only those matching the query text.
                .filter(isItemMatching)
                // Return 10 items max - needed for generic queries when the list may contain hundreds of elements.
                .slice(0, 10);

            resolve(itemsToDisplay);
        }, 10);
    });

    // Filtering function - it uses `name` and `username` properties of an item to find a match.
    function isItemMatching(item) {
        // Make the search case-insensitive.
        const searchString = queryText.toLowerCase();

        // Include an item in the search results if name or username includes the current user input.
        return (
            item.name.toLowerCase().includes(searchString) ||
            item.id.toLowerCase().includes(searchString)
        );
    }
}

function customItemRenderer(item) {
    const itemElement = document.createElement('span');
    const toolElement = document.createElement('span');
    const noteElement = document.createElement('span');
    const idElement = document.createElement('span');

    itemElement.classList.add('custom-item');

    toolElement.classList.add('custom-item-tool');
    toolElement.textContent = `${item.name}`;

    noteElement.classList.add('custom-item-note');
    noteElement.textContent = `${item.note}`;

    idElement.classList.add('custom-item-id');
    idElement.textContent = item.id;

    itemElement.appendChild(toolElement);
    itemElement.appendChild(noteElement);
    itemElement.appendChild(idElement);

    return itemElement;
}

const mentionItems = [
    {
        name: 'Compiler',
        id: '#\console.log(콘솔 로그)',
        note: ''
    },
    {
        name: 'Compiler',
        id: '#\Build(빌드 명령어)',
        note: ''
    },
    {
        name: 'COVER',
        id: '#\<COVER 페이지>/사용하는 툴체인 정보',
        note: '예제) Visual Studio 2017 64bit, 툴체인 정보들'
    },
    {
        name: 'COVER',
        id: '#\<COVER 설치 경로>/external/bin/tmpa.exe',
        note: '분석기 정보'
    },
    {
        name: 'COVER EE',
        id: '#\<COVER EE 설치 경로>/cover-server/tomcat-*/logs/taskManager.log',
        note: 'COVER EE 로그'
    },
    {
        name: 'COVER EE',
        id: '#\<COVER EE 설치 경로>/cover-server/tomcat-*/logs/remoteService.log',
        note: 'COVER EE 로그'
    },
    {
        name: 'COVER EE',
        id: '#\<COVER EE 설치 경로>/external/workspace/error/<해시값을 가지는 폴더명>/log/parse/<파일명>',
        note: 'COVER EE 빌드 파일들'
    },
    {
        name: 'COVER EE Agent',
        id: '#\<COVER Agent 설치 경로>/logs/testmonitor.log',
        note: 'COVER EE Agent 로그'
    },
    {
        name: 'COVER EE Agent',
        id: '#\<COVER Agent 설치 경로>/logs/agent-control.log',
        note: 'COVER EE Agent 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/build/CoverAnalyzer.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/build/CoverBuilder.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/build/CoverCalculator.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/build/CoverEtc.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/build/CoverEtc.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/UI/CoverCalculator.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/UI/CoverEtc.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/UI/CoverInfra.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/UI/coverReport.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/logs/UI/CoverUI.log',
        note: 'COVER SE 로그'
    },
    {
        name: 'COVER SE',
        id: '#\<COVER SE 설치 경로>/external/workspace/error/<해시값을 가지는 폴더>/log/parse/<파일명>',
        note: 'COVER SE 빌드 분석 파일들'
    },


    {
        name: 'STATIC',
        id: '#\C:/Users/user/Documents/.static_home',
        note: 'STATIC_HOME(STATIC 설치 경로)'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/Documents/.static_home/workspace\web-ui\logs',
        note: 'STATIC web-ui 경로'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox',
        note: 'STATIC Toolbox 경로'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox/apps/STATIC Client for C, C++ v<버전>',
        note: 'STATIC Client for C, C++ 경로'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox/apps/STATIC Client for C# v<버전>',
        note: 'STATIC Client for C# 경로'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox/apps/STATIC Client for Java v<버전>',
        note: 'STATIC Client for Java 경로'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox/apps/STATIC Analysis Agent v<버전>/bin/Psionic/Psionic.exe',
        note: 'STATIC CWE 분석기 파일'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/AppData/Local/Suresofttech/STATIC Toolbox/apps/STATIC Analysis Agent v<버전>/bin/ci.exe',
        note: 'STATIC MISRA 분석기 파일#'
    },
    {
        name: 'STATIC',
        id: '#\C:/Users/user/Documents/.static_home',
        note: 'STATIC_HOME(STATIC 설치 경로)'
    },

    {
        name: 'STATIC Server Web Page',
        id: '#STATIC Server Project History',
        note: 'STATIC Server 웹 페이지 History'
    },

    {
        name: 'STATIC Client',
        id: '#STATIC Client Command',
        note: 'STATIC Client 빌드 명령어'
    },
    {
        name: 'STATIC Client',
        id: '#STATIC Client Configuration to add',
        note: 'STATIC Client 설정'
    },
    {
        name: 'STATIC Client',
        id: '#STATIC Client Validation Error',
        note: 'STATIC Client 빌드 오류'
    },
];

export {
    editor,
    editorConfig,
    editorGeneralData,
    editorRuleData,
    editorExampleData,
    editorGuidelineData,
    editorToolConfigurationData,
    editorToolTroubleShootingData,
    editorToolUsageMethodData,
    editorDynamicTestData,
    editorFinalTestData,
    editorEducationData,
    editorReportData
};