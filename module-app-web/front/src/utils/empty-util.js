/* 객체 empty 여부 반환 */
const isEmpty = (obj) => {
    for (let prop in obj) {
        if (obj.hasOwnProperty(prop))
            return false;
    }

    return true;
}

export {isEmpty};