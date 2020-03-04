<template>
    <div>
        <el-collapse-transition>
        <div v-show="showTable" :class="isCollapse">
        <el-button @click="resetProfFilter">Удалить фильтр профкома</el-button>
        <el-button @click="clearFilter">Удалить все фильтры</el-button>

        <el-table
            ref="tab"
            :data="tableData.filter(
                data => !search || data.pib.toLowerCase().includes(search.toLowerCase())
                || data.code.toLowerCase().includes(search.toLowerCase())
                || data.group.toLowerCase().includes(search.toLowerCase())
                )"

            :default-sort="{prop: 'group', order: 'descending'}"
            :row-class-name="tableRowClassName"
            lazy
            max-height="500px">
        <el-table-column type="expand">
            <template slot-scope="props">
                <p>Пол: <el-switch
                        style="display: block"
                        v-model="props.row.gender"
                        active-color="#F000D7"
                        inactive-color="#00A9F9"
                        active-text="Ж"
                        inactive-text="М"
                        @change="genderUpdate(props.row)">
                </el-switch></p>
                <p>Дата рождения: {{ props.row.date }}</p>
                <p>Адрес: {{ props.row.address }}</p>
            </template>
        </el-table-column>

        <el-table-column
                fixed
                label="Чл. Проф."
                :filters="[{ text: 'Да', value: true }, { text: 'Нет', value: false }]"
                :filter-method="filterProf"
                filter-placement="bottom-end"
                column-key="profCol"
                width="100">
            <template slot-scope="scope">
                <el-switch v-model="scope.row.prof" @change="profUpdate(scope.row)"> </el-switch>
            </template>

        </el-table-column>
        <el-table-column
                prop="pib"
                label="ФИО"
                sortable
                width="250">
        </el-table-column>
        <el-table-column
                prop="group"
                label="Группа"
                sortable
                width="120">
        </el-table-column>
        <el-table-column
                prop="code"
                label="Идент. код"
                width="120">
        </el-table-column>

            <el-table-column
                    :key="facFilter.facId"
                    :filters="facFilter"
                    :filter-method="filterFac"
                prop="faculty.abbreviation"
                label="Факультет"
                width="120">
            </el-table-column>

            <el-table-column
                    prop="tag"
                    label="Tag"
                    width="100"
                    :filters="[{ text: 'бюджет', value: 'бюджет' }, { text: 'контракт', value: 'контракт' }]"
                    :filter-method="filterTag"
                    filter-placement="bottom-end">
                <template slot-scope="scopeTag">
                    <el-tag
                            :type="scopeTag.row.tag"
                            disable-transitions>{{scopeTag.row.trainingForm}}</el-tag>
                </template>
            </el-table-column>

        <el-table-column
                align="right">
            <template slot="header" slot-scope="scope">
                <el-input
                        @click="handleEdit(scope.$index, scope.row)"
                        v-model="search"
                        size="mini"
                        placeholder="Type to search"/>
            </template>
            <template slot-scope="scope">
                <el-button
                        size="mini"
                        @click="handleEdit(scope.$index, scope.row)">Редактировать</el-button>
                <el-button
                        size="mini"
                        type="danger"
                        @click.native.prevent="deleteRow(scope.$index, tableData, scope.row.idStud)">Удалить</el-button>
            </template>
        </el-table-column>

    </el-table>
            <div>
            <el-button type="primary" @click="add()">Добавить</el-button>
            <el-button type="primary" @click="reportCreate()">Отчёт</el-button>
            </div>
            <upload-file :faculty-data="facultyData"> </upload-file>
            <div><el-button @click="showFunc()">Список факультетов</el-button></div>
        </div>
        </el-collapse-transition>


        <el-drawer
                title="Введите необходимые данные"
                :before-close="handleCloseAdd"
                :visible.sync="showAdd"
                direction="ltr"
                custom-class="drawer"
                ref="drawerAdd"
                :with-header="false"
                size="50%"
        >
        <div style="padding: 20px">
        <el-form ref="form" :model="formCreate" label-width="120px">
            <el-form-item label="ФИО">
                <el-input v-model="formCreate.pib"> </el-input>
            </el-form-item>
            <el-form-item label="Группа">
                    <el-input v-model="formCreate.group"> </el-input>
            </el-form-item>
            <el-form-item label="Пол">
                <el-switch
                        style="display: block"
                        v-model="formCreate.gender"
                        active-color="#F000D7"
                        inactive-color="#00A9F9"
                        active-text="Женский"
                        inactive-text="Мужской">
                </el-switch>
            </el-form-item>
            <el-form-item label="Идентификационный Код" label-width="200px">
                <el-input v-model="formCreate.code"> </el-input>
            </el-form-item>
            <el-form-item label="Форма обучения">
                <el-radio-group v-model="formCreate.trainingForm">
                    <el-radio label="контракт"> </el-radio>
                    <el-radio label="бюджет"> </el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="Факультет">
                <el-select v-model="formCreate.faculty" placeholder="Select">
                    <el-option
                            v-for="item in facultyData"
                            :key="item.facId"
                            :label="item.abbreviation"
                            :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="Дата рождения">
                <el-col :span="11">
                    <el-date-picker type="date" placeholder="Pick a date" v-model="formCreate.date" style="width: 100%;"> </el-date-picker>
                </el-col>
            </el-form-item>
            <el-form-item label="Адрес">
                <el-input v-model="formCreate.address"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit()">Сохранить</el-button>
                <el-button @click="handleCloseAdd()">Отмена</el-button>
            </el-form-item>
        </el-form>
    </div>
        </el-drawer>


        <el-drawer
                title="Введите необходимые данные"
                :before-close="handleCloseUpdate"
                :visible.sync="showUpdate"
                direction="ltr"
                custom-class="drawer"
                ref="drawerAdd"
                :with-header="false"
                size="50%"
        >
            <div style="padding: 20px">
            <el-form
                    ref="form" :model="formUpdate"
                    size="mini"
                    label-width="120px"
            >
                <el-form-item label="ФИО">
                    <el-input v-model="formUpdate.pib"> </el-input>
                </el-form-item>
                <el-form-item label="Группа">
                    <el-input v-model="formUpdate.group"> </el-input>
                </el-form-item>
                <el-form-item label="Пол">
                    <el-switch
                            style="display: block"
                            v-model="formUpdate.gender"
                            active-color="#F000D7"
                            inactive-color="#00A9F9"
                            active-text="Женский"
                            inactive-text="Мужской">
                    </el-switch>
                </el-form-item>
                <el-form-item label="Идентификационный Код" label-width="200px">
                    <el-input v-model="formUpdate.code"> </el-input>
                </el-form-item>
                <el-form-item label="Членство в профкоме" label-width="200px">
                    <el-switch v-model="formUpdate.prof"> </el-switch>
                </el-form-item>
                <el-form-item label="Форма обучения">
                    <el-radio-group v-model="formUpdate.trainingForm">
                        <el-radio label="контракт"> </el-radio>
                        <el-radio label="бюджет"> </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="Факультет">
                    <el-select v-model="formUpdate.faculty" placeholder="Select">
                        <el-option
                                v-for="item in facultyData"
                                :key="item.facId"
                                :label="item.abbreviation"
                                :value="item.facId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="Дата рождения">
                    <el-col :span="11">
                        <el-date-picker
                                type="date"
                                placeholder="Выберите дату"
                                v-model="formUpdate.date"
                                style="width: 100%;"
                                @change="changeDate"
                        > </el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="Адрес">
                    <el-input v-model="formUpdate.address"> </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmitUpdate()">Сохранить</el-button>
                    <el-button @click="handleCloseUpdate()">Отмена</el-button>
                </el-form-item>
            </el-form>

            </div>
        </el-drawer>
    </div>


</template>

<script>
    import { allStudents,
        deleteStudent,
        addStudent,
        updateStud,
        updateGender,
        updateProf,
        getReportStudent} from '../api.js';
    import UploadFile from "./UploadFile";
    export default {
        name: "StudentTable",
        components: {
            UploadFile
        },
        props:{
          facultyData:[],
            showFunc: {type: Function},
            facFilter:[],
            isCollapse: {}
        },
        mounted() {
           // alert(this.isCollapse);
            allStudents().then(res => res.data.forEach(el=>{
                var dat = [''];
                if(el.date!==null){dat = el.date.split("T");}
                const val = {
                    idStud: el.idStud,
                    prof: el.prof,
                    pib:el.pib,
                    group:el.groupE,
                    gender:el.gender,
                    code:el.code,
                    date: dat[0],
                    address:el.address,
                    trainingForm: el.trainingForm,
                    faculty: el.faculty,
                    tag: 'danger'};
                if ( val.trainingForm === null) {
                    val.trainingForm = 'помилка'
                }
                if(val.trainingForm === 'бюджет'){
                    val.tag = 'success'
                }
                if(val.trainingForm === 'контракт'){
                    val.tag = ''
                }
                this.tableData.push(val)}));
            //console.log(this.tableData);
        },
        methods: {
            reportCreate(){
              getReportStudent().then(res=>alert(res.data));
            },
            updateCheck(){
                if(this.formUpdate.code.length!==10){
                    return 'Идент. код должен состоять из 10 цифр';
                }
                for(var i1=0;i1<this.formUpdate.code.length;i1++){
                    if(this.formUpdate.code[i1]<'0' || this.formUpdate.code[i1]>'9'){
                        //alert('alert1');
                        return 'Идент. код должен состоять только из цифр';
                    }
                }
                for(var i2=0;i2<this.formUpdate.pib.length;i2++) {
                    if (this.formUpdate.pib[i2].toLowerCase() < 'а' ||
                        this.formUpdate.pib[i2].toLowerCase() > 'я' &&
                        this.formUpdate.pib[i2] !== '’' &&
                        this.formUpdate.pib[i2] !== "'") {
                        // alert('alert2 '+row.pib[i2].toLowerCase());
                        return 'ФИО должно состоять только из букв';
                    }
                }
                if(this.formUpdate.pib.length===0){return 'Заполните поле ФИО'}
                if(this.formUpdate.group.length===0){return 'Заполните поле группы'}
                if(this.formUpdate.trainingForm===''){return 'Выберите форму обучения'}
                if(this.formUpdate.faculty.facId===''){return 'Выберите факультет'}
                return '';
            },
            addCheck(){
                if(this.formCreate.code.length!==10){
                    return 'Идент. код должен состоять из 10 цифр';
                }
                for(var i1=0;i1<this.formCreate.code.length;i1++){
                    if(this.formCreate.code[i1]<'0' || this.formCreate.code[i1]>'9'){
                        //alert('alert1');
                        return 'Идент. код должен состоять только из цифр';
                    }
                }
                for(var i2=0;i2<this.formCreate.pib.length;i2++) {
                    if (this.formCreate.pib[i2].toLowerCase() < 'а' ||
                        this.formCreate.pib[i2].toLowerCase() > 'я' &&
                        this.formCreate.pib[i2] !== '’' &&
                        this.formCreate.pib[i2] !== "'") {
                        // alert('alert2 '+row.pib[i2].toLowerCase());
                        return 'ФИО должно состоять только из букв';
                    }
                }
                if(this.formCreate.pib.length===0){return 'Заполните поле ФИО'}
                if(this.formCreate.group.length===0){return 'Заполните поле группы'}
                if(this.formCreate.trainingForm===''){return 'Выберите форму обучения'}
                if(this.formCreate.faculty.facId===''){return 'Выберите факультет'}
                return '';
            },
            handleCloseUpdate(){
                this.hideUpdate();
                this.$message({
                    message: 'Редактирование отменено',
                    center: true
                });
            },
            handleCloseAdd(){
                this.hideCreate();
                this.$message({
                    message: 'Добавление отменено',
                    center: true
                });
            },
            tableRowClassName({row, rowIndex}){
               // alert("did "+rowIndex);
                if(row.code.length!==10){
                    return 'warning-row';
                }
                for(var i1=0;i1<row.code.length;i1++){
                    if(row.code[i1]<'0' || row.code[i1]>'9'){
                        //alert('alert1');
                        return 'warning-row';
                    }
                }
                for(var i2=0;i2<row.pib.length;i2++){
                    if(row.pib[i2].toLowerCase()<'а' ||
                        row.pib[i2].toLowerCase()>'я' &&
                        row.pib[i2]!=='’' &&
                        row.pib[i2]!=="'"){
                       // alert('alert2 '+row.pib[i2].toLowerCase());
                        return 'warning-row';
                    }
                }
                if(row.tag==='danger'){
                    return 'warning-row';
                }
                return '';

            },
            filterTag(value, row) {
                return row.trainingForm === value;
            },

            changeDate(){
                console.log(this.formUpdate.date);
            },
            deleteRow(index, rows, idLoc) {
                deleteStudent(idLoc).then(res=>{
                    if (res.data!=null) {
                        for( var i = 0; i < rows.length; i++){
                            if ( rows[i].idStud === idLoc) {
                                rows.splice(i, 1);
                                this.$message({
                                    message: 'Успешно удалено',
                                    type: 'success'
                                });
                            }
                        }
                    }
                    else {alert("error");}
                });

            },
            genderUpdate(row){
                updateGender(row.idStud, row.gender).then(res=>{
                    if(res.data!=null){
                        for( var i = 0; i < this.tableData.length; i++){
                            if ( this.tableData[i].idStud === res.data.idStud) {
                               this.tableData[i].gender = res.data.gender;
                            }
                        }
                    }
                    else {
                        this.$message.error('Ошибка при редактировании');
                    }
                })
            },

            profUpdate(row){
                updateProf(row.idStud, row.prof).then(res=>{
                    if(res.data!=null){
                        for( var i = 0; i < this.tableData.length; i++){
                            if ( this.tableData[i].idStud === res.data.idStud) {
                                this.tableData[i].prof = res.data.prof;
                            }
                        }

                    }
                    else {
                        this.$message.error('Ошибка при редактировании');
                    }
                })
            },

            add(){
                if(this.facultyData.length!==0){
                    this.formCreate.faculty.facId = this.facultyData[0].facId;
                    this.formCreate.faculty.facName = this.facultyData[0].facName;
                    this.formCreate.faculty.abbreviation = this.facultyData[0].abbreviation;
                    this.showAdd=true;
                }
                else{
                    this.$message.error('Нету данных факультетов');
                    this.showFunc();
                }
            },
            hideCreate(){
                    this.formCreate.pib = '';
                this.formCreate.group = '';
                this.formCreate.gender = false;
                this.formCreate.code = '';
                this.formCreate.date = '';
                this.formCreate.address = '';
                this.formCreate.trainingForm = '';
                this.formCreate.faculty.facId = this.facultyData[0].facId;
                this.formCreate.faculty.facName = this.facultyData[0].facName;
                this.formCreate.faculty.abbreviation = this.facultyData[0].abbreviation;
                this.showAdd = false;
            },
            hideUpdate(){
                this.formUpdate.idStud = '';
                this.formUpdate.pib = '';
                this.formUpdate.group = '';
                this.formUpdate.gender = false;
                this.formUpdate.prof = false;
                this.formUpdate.code ='';
                this.formUpdate.date = '';
                this.formUpdate.address = '';
                this.formUpdate.trainingForm = '';
                this.formUpdate.faculty.facId = this.facultyData[0].facId;
                this.formUpdate.faculty.facName = this.facultyData[0].facName;
                this.formUpdate.faculty.abbreviation = this.facultyData[0].abbreviation;
                this.showUpdate = false;
            },
            onSubmit(){
                const checkRes = this.addCheck();
                if(checkRes==='') {
                    if (this.formCreate.date===''){
                        this.formCreate.date = null;
                    }
                    addStudent(
                        this.formCreate.pib,
                        this.formCreate.group,
                        this.formCreate.gender,
                        this.formCreate.code,
                        this.formCreate.date,
                        this.formCreate.address,
                        this.formCreate.trainingForm,
                        this.formCreate.faculty.facId).then(res => {
                        if (res.data != null) {
                            this.formCreate.pib = '';
                            this.formCreate.group = '';
                            this.formCreate.gender = false;
                            this.formCreate.code = '';
                            this.formCreate.date = '';
                            this.formCreate.address = '';
                            this.formCreate.trainingForm = '';
                            this.formCreate.faculty.facId = this.facultyData[0].facId;
                            this.formCreate.faculty.facName = this.facultyData[0].facName;
                            this.formCreate.faculty.abbreviation = this.facultyData[0].abbreviation;
                            this.showAdd = false;
                            this.showTable = true;
                            console.log(res.data.faculty);
                            var dat = [''];
                            if(res.data.date!==null){dat = res.data.date.split("T");}

                            const val = {
                                idStud: res.data.idStud,
                                prof: res.data.prof,
                                pib: res.data.pib,
                                group: res.data.groupE,
                                gender: res.data.gender,
                                code: res.data.code,
                                date: dat[0],
                                address: res.data.address,
                                trainingForm: res.data.trainingForm,
                                faculty: res.data.faculty
                            };

                            this.tableData.push(val);
                            this.$message({
                                message: 'Успешно добавлено',
                                type: 'success'
                            });
                            //this.updateTab();
                        } else {
                            alert("error!");
                        }
                    });
                }
                else{
                    this.$message.error(checkRes);
                }

            },
            onSubmitUpdate(){
                const resCheck = this.updateCheck();
                if(resCheck===''){
                if (this.formUpdate.date===''){
                    this.formUpdate.date = null;
                }
                updateStud(this.formUpdate.idStud,
                    this.formUpdate.prof,
                    this.formUpdate.pib,
                    this.formUpdate.group,
                    this.formUpdate.gender,
                    this.formUpdate.code,
                    this.formUpdate.date,
                    this.formUpdate.address,
                    this.formUpdate.trainingForm,
                    this.formUpdate.faculty.facId).then(res=>{
                        if (res.data!=null){
                            this.hideUpdate();
                            var dat = [''];
                            if(res.data.date!==null){dat = res.data.date.split("T");}
                            for( var i = 0; i < this.tableData.length; i++){
                                if ( this.tableData[i].idStud === res.data.idStud) {
                                    this.tableData[i].prof = res.data.prof;
                                    this.tableData[i].pib = res.data.pib;
                                    this.tableData[i].group = res.data.groupE;
                                    this.tableData[i].gender = res.data.gender;
                                    this.tableData[i].code = res.data.code;
                                    this.tableData[i].date = dat[0];
                                    this.tableData[i].address = res.data.address;
                                    this.tableData[i].trainingForm = res.data.trainingForm;
                                    this.tableData[i].faculty = res.data.faculty;
                                }
                            }
                            this.$message({
                                message: 'Успешно изменено',
                                type: 'success'
                            });
                        }
                        else {
                            this.$message.error('Ошибка при редактировании');
                        }
                })}
                else {
                    this.$message.error(resCheck);
                }
            },
            filterProf(value, row){
                return row.prof === value;
            },
            filterFac(value, row){
                console.log(row);
                console.log(value);
                if (row.faculty===null) return false;
                return row.faculty.facId === value;
            },
            resetProfFilter(){
                this.$refs.tab.clearFilter('profCol');
            },
            clearFilter() {
                this.$refs.tab.clearFilter();
            },
            handleEdit(index, row) {
                if(this.facultyData.length!==0) {
                    this.formUpdate.idStud = row.idStud;
                    this.formUpdate.pib = row.pib;
                    this.formUpdate.group = row.group;
                    this.formUpdate.gender = row.gender;
                    this.formUpdate.prof = row.prof;
                    this.formUpdate.code = row.code;
                    if(row.date!==''){this.formUpdate.date = row.date + "T00:00:00Z";}
                    else{this.formUpdate.date='';}

                    //console.log(this.formUpdate.date);
                    this.formUpdate.address = row.address;
                    if (row.faculty != null) {
                        this.formUpdate.faculty.facId = row.faculty.facId;
                        this.formUpdate.faculty.facName = row.faculty.facName;
                        this.formUpdate.faculty.abbreviation = row.faculty.abbreviation;
                    }
                    else {
                        this.formUpdate.faculty.facId = this.facultyData[0].facId;
                        this.formUpdate.faculty.facName = this.facultyData[0].facName;
                        this.formUpdate.faculty.abbreviation = this.facultyData[0].abbreviation;
                    }
                    this.formUpdate.trainingForm = row.trainingForm;
                    this.showUpdate = true;
                }
                else{
                    this.$message.error('Нету данных факультетов');
                    this.showFunc();
                }
            }


        },
        data() {
            return {
                formCreate: {
                    pib: '',
                    group: '',
                    gender:false,
                    code:'',
                    date: '',
                   address:'',
                    trainingForm: '',
                    faculty:{
                        facId: '',
                        facName: '',
                        abbreviation: ''
                    }
                },
                tableData: [],
                showAdd:false,
                showUpdate: false,
                showTable: true,
                search: '',
                formUpdate: {
                    idStud: '',
                    pib: '',
                    group: '',
                    prof: false,
                    gender:false,
                    code:'',
                    date: '',
                    address:'',
                    trainingForm: '',
                    faculty:{
                        facId: '',
                        facName: '',
                        abbreviation: ''
                    }
                }
            }
        }
    }

</script>

<style>
    .forms{
        border: 1px solid;
        border-radius: 4px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        position: fixed;
        padding: 20px;
        margin: 0 30vw;
        top:5px;
    }
    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }
    .floatRightCollapsed{
        animation-name: collap;
        animation-duration: 0.5s;
        animation-timing-function: linear;
        float: right;
    }
    .floatRightNotCollapsed{
        animation-name: uncollap;
        animation-duration: 0.5s;
        animation-timing-function: linear;
        float: right;
    }
    @keyframes collap {
        from{
            width: 84%;
        }
        to{
            width: 95%;
        }
    }
    @keyframes uncollap {
        from{
            width: 95%;
        }
        to{
            width: 84%;
        }
    }
</style>
