import axios from 'axios';
//REFERENCE: book_user has userid, isbn, mins read, +++

export default {

    // getAll() {
    //     return axios.get('/book');
    // },

    getBookUsersByUserId(id){
        return axios.get(`/stats/user/${id}`);//list of bookuser(s) ??
    },

    getBookByIsbn(isbn){
        return axios.get(`/book/${isbn}`);
    },

    addBook(book) {
        return axios.post('/book', book);
    },

    addBookForCurrentUser(bookUser) {
        return axios.post('/stats', bookUser);
    },

    getAllBookUserInfoByFamilyId(familyId) {
        return axios.get(`/stats/family/${familyId}`);
    },

    updateBookUser(updatedBookUser, userId, isbn) {
        return axios.put(`/stats/user/${userId}/book/${isbn}`, updatedBookUser);
    }
}