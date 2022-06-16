export const pageInfo = {
    namespaced: true,
    state: () => ({
        searchType: null,
        previousSearchType: null,
        searchKeyword: null,
        page: null,
        pageName: null
    }),
    mutations: {
        setPageInfo(state, payload) {
            // console.log("\n\n\nsetPageInfo");
            // console.log(payload)
            // console.log(payload.searchType)
            // console.log(payload.searchKeyword)
            // console.log(payload.page)
            // console.log(payload.pageName)

            state.searchType = payload.searchType;
            state.previousSearchType = payload.previousSearchType;
            state.searchKeyword = payload.searchKeyword;
            state.page = payload.page;
            state.pageName = payload.pageName;
        }
    },
    getters: {
        getPageInfo(state) {
            // console.log("\n\n\ngetPageInfo");
            // console.log(state.searchType)
            // console.log(state.previousSearchType)
            // console.log(state.searchKeyword)
            // console.log(state.page)
            // console.log(state.pageName)
            // console.log("\n")
        }
    },
    actions: {

    }
}
