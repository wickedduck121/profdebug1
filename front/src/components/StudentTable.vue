<template>
    <div>
        <el-collapse-transition>
        <div v-show="showTable">
        <el-button @click="resetProfFilter">Удалить фильтр профкома</el-button>
        <el-button @click="clearFilter">Удалить все фильтры</el-button>

        <el-table

            ref="tab"
            :data="tableData.filter(
                data => !search || data.pib.toLowerCase().includes(search.toLowerCase())
                || data.code.toLowerCase().includes(search.toLowerCase())
                || data.group.toLowerCase().includes(search.toLowerCase())
                )"
            style="width: 100%"
            :default-sort="{prop: 'group', order: 'descending'}"
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
                        @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                <el-button
                        size="mini"
                        type="danger"
                        @click.native.prevent="deleteRow(scope.$index, tableData, scope.row.idStud)">Delete</el-button>
            </template>
        </el-table-column>

    </el-table>
            <el-button type="primary" @click="add()">Добавить</el-button>
            <upload-file> </upload-file>
        </div>
        </el-collapse-transition>


        <el-collapse-transition>
        <div class="forms" v-show="showAdd">
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
            <el-form-item label="Дата рождения">
                <el-col :span="11">
                    <el-date-picker type="date" placeholder="Pick a date" v-model="formCreate.date" style="width: 100%;"> </el-date-picker>
                </el-col>
            </el-form-item>
            <el-form-item label="Адрес">
                <el-input v-model="formCreate.address"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit()">Create</el-button>
                <el-button @click="hideCreate()">Cancel</el-button>
            </el-form-item>
        </el-form>
    </div>
        </el-collapse-transition>


        <el-collapse-transition>
            <div v-show="showUpdate" class="forms">
            <el-form
                    ref="form" :model="formUpdate"
                    label-width="120px"
                    size="mini"
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
                    <el-button @click="hideUpdate()">Отмена</el-button>
                </el-form-item>
            </el-form>

            </div>
        </el-collapse-transition>
    </div>


</template>

<script>
    import { allStudents,  deleteStudent, addStudent, updateStud, updateGender, updateProf } from '../api.js';
    import UploadFile from "./UploadFile";
    export default {
        name: "StudentTable",
        components: {
            UploadFile
        },
        mounted() {
            allStudents().then(res => res.data.forEach(el=>{
                var dat = el.date.split("T");
                const val = {
                    idStud: el.idStud,
                    prof: el.prof,
                    pib:el.pib,
                    group:el.groupE,
                    gender:el.gender,
                    code:el.code,
                    date: dat[0],
                    address:el.address};

                this.tableData.push(val)}));
        },
        methods: {
            changeDate(){
                console.log(this.formUpdate.date);
            },
            deleteRow(index, rows, idLoc) {
                deleteStudent(idLoc).then(res=>{
                    if (res.data!=null) {
                        for( var i = 0; i < rows.length; i++){
                            if ( rows[i].idStud === idLoc) {
                                rows.splice(i, 1);
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
                        alert("error updating");
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
                        alert("error updating");
                    }
                })
            },

            add(){
                this.showTable=false;
                this.showAdd=true;

            },
            hideCreate(){
                    this.formCreate.pib= '';
                this.formCreate.group= '';
                this.formCreate.gender=false;
                this.formCreate.code='';
                this.formCreate.date= '';
                this.formCreate.address='';
                this.showAdd = false;
                this.showTable = true;
            },
            hideUpdate(){
                this.formUpdate.idStud= '';
                this.formUpdate.pib= '';
                this.formUpdate.group= '';
                this.formUpdate.gender=false;
                this.formUpdate.prof=false;
                this.formUpdate.code='';
                this.formUpdate.date= '';
                this.formUpdate.address='';
                this.showUpdate = false;
                this.showTable = true;
            },
            onSubmit(){
                addStudent(
                    this.formCreate.pib,
                    this.formCreate.group,
                    this.formCreate.gender,
                    this.formCreate.code,
                    this.formCreate.date,
                    this.formCreate.address).then(res=>{
                        if (res.data!=null){
                            this.formCreate.pib= '';
                            this.formCreate.group= '';
                            this.formCreate.gender=false;
                            this.formCreate.code='';
                            this.formCreate.date= '';
                            this.formCreate.address='';
                            this.showAdd = false;
                            this.showTable = true;

                            var dat = res.data.date.split("T");
                            const val = {
                                idStud: res.data.idStud,
                                prof: res.data.prof,
                                pib:res.data.pib,
                                group:res.data.group,
                                gender:res.data.gender,
                                code:res.data.code,
                                date: dat[0],
                                address:res.data.address};

                            this.tableData.push(val);

                            //this.updateTab();
                        }
                        else{
                            alert("error!");
                        }
                });

            },
            onSubmitUpdate(){
                updateStud(this.formUpdate.idStud,
                    this.formUpdate.prof,
                    this.formUpdate.pib,
                    this.formUpdate.group,
                    this.formUpdate.gender,
                    this.formUpdate.code,
                    this.formUpdate.date,
                    this.formUpdate.address).then(res=>{
                        if (res.data!=null){
                            this.hideUpdate();
                            var dat = res.data.date.split("T");
                            for( var i = 0; i < this.tableData.length; i++){
                                if ( this.tableData[i].idStud === res.data.idStud) {
                                    this.tableData[i].prof = res.data.prof;
                                    this.tableData[i].pib = res.data.pib;
                                    this.tableData[i].group = res.data.groupE;
                                    this.tableData[i].gender = res.data.gender;
                                    this.tableData[i].code = res.data.code;
                                    this.tableData[i].date = dat;
                                    this.tableData[i].address = res.data.address;
                                }
                            }
                        }
                        else {
                            alert("error")
                        }
                })
            },
            filterProf(value, row){
                return row.prof === value;
            },
            resetProfFilter(){
                this.$refs.tab.clearFilter('profCol');
            },
            clearFilter() {
                this.$refs.tab.clearFilter();
            },
            handleEdit(index, row) {
                this.formUpdate.idStud = row.idStud;
                this.formUpdate.pib = row.pib;
                this.formUpdate.group = row.group;
                this.formUpdate.gender = row.gender;
                this.formUpdate.prof = row.prof;
                this.formUpdate.code = row.code;
                this.formUpdate.date = row.date+"T00:00:00Z";
                //console.log(this.formUpdate.date);
                this.formUpdate.address = row.address;

                this.showTable = false;
                this.showUpdate = true;
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
                   address:''
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
                    address:''
                }
            }
        }
    }

</script>

<style scoped>
    .forms{
        border: 1px solid;
        border-radius: 4px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        position: fixed;
        padding: 20px;
        margin: 0 30vw
    }
</style>