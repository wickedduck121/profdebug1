import axios from 'axios'

export function allStudents() {
    return axios.get('/api/students');
}

export function addStudent(pib, group, gender, code, date, address) {
    return axios.post('/api/students',{ pib:pib, groupE: group, gender:gender, code:code, date:date, address:address, prof:false })
}

export function deleteStudent(id_local) {
    var url = '/api/students/';
    return axios.delete(url+id_local);
}

export function  login(name_loc,pass_loc) {
    axios.post('/api/login',{},{params:{username:name_loc,password:pass_loc}});

}