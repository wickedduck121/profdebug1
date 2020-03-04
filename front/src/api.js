import axios from 'axios'

export function allStudents() {
    return axios.get('/api/students');
}

export function addStudent(pib, group, gender, code, date, address, trainingForm,facId) {
    console.log("facId "+facId);
    return axios.post('/api/students',{
        pib:pib,
        groupE: group,
        gender:gender,
        code:code,
        date:date, address:address,
        prof:false,
        trainingForm: trainingForm,
        facId: facId})
}

export function deleteStudent(id_local) {
    var url = '/api/students/del/';
    return axios.delete(url+id_local);
}

export function  login(name_loc,pass_loc) {
    return axios.post('/api/login',{},{params:{username:name_loc,password:pass_loc}});
}
export function  register(usNameLoc, passLoc, emailLoc) {
    return axios.post('/api/user/reg',{usName:usNameLoc,pass:passLoc, email:emailLoc});
}

export function  getDoc() {
    return axios.get('/api/students/doc');

}

/*export function  sendDoc(file) {
    var form = new FormData();
    form.append("file", file);
    console.log(file);
    console.log(form);
    //alert(file.content);
    return axios.post('/api/files',{}, {params:{file:form}});

}*/

export function  sendDoc(file) {
    var form = new FormData();
    form.append("file", file);
    console.log(file);
    console.log(form);

    //alert(file.content);
    return axios.post('/api/files',{}, {params:{file: file}});
}

export function updateStud(idStud, prof, pib, groupE, gender, code, date, address, trainingForm,facId) {
    console.log({idStud,prof,pib,groupE,gender,code,date,address,trainingForm,facId});
    return axios.put('api/students/'+idStud,
        {idStud: idStud,
        prof: prof,
        pib: pib,
        groupE: groupE,
        gender: gender,
        code: code,
        date: date,
        address: address,
            trainingForm: trainingForm,
        facId: facId})
}

export function updateGender(idStud, genderLoc) {
 return axios.put('api/students/gender/'+idStud, {genderLoc})
}
export function updateProf(idStud, profLoc) {
    return axios.put('api/students/prof/'+idStud, {profLoc})
}

export function addFaculty(facName, abbreviation) {
    return axios.post('api/faculty',{facName,abbreviation})
}
export function delFaculty(idFac) {
    return axios.delete('api/faculty/'+idFac)
}
export function updateFaculty(facId, facName, abbreviation) {
    return axios.put('api/faculty', {facId, facName, abbreviation})
}
export  function getAllFaculty() {
    return axios.get('api/faculty')
}
export  function getReportStudent() {
    return axios.get('api/report/student')

}

