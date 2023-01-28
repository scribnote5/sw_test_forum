let codeMirror = {
    editorData: "/* test.h */" + "\n" + "// 설명 및 코드 작성" + "\n\n\n" + "/* test.c */" + "\n" + "// 설명 및 코드 작성",
    nonCompliantExample: "",
    compliantExample: "",
    badCasePositionList: [],
    goodCasePositionList: [],

    setNonCompliantExample(nonCompliantExample) {
        this.nonCompliantExample = nonCompliantExample;
    },
    getNonCompliantExample() {
        return this.nonCompliantExample;
    },
    setCompliantExample(compliantExample) {
        this.compliantExample = compliantExample;
    },
    getCompliantExample() {
        return this.compliantExample;
    },
    setBadCasePositionList(badCasePositionList) {
        this.badCasePositionList = badCasePositionList;
    },
    getBadCasePositionList() {
        return this.badCasePositionList;
    },
    setGoodCasePositionList(goodCasePositionList) {
        this.goodCasePositionList = goodCasePositionList;
    },
    getGoodCasePositionList() {
        return this.goodCasePositionList;
    },

    initCodeMirror() {
        this.compliantExample = "";
        this.nonCompliantExample = "";
        this.badCasePositionList = [];
        this.goodCasePositionList = [];
    }
}

export {codeMirror};