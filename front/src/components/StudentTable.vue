<template>
    <div>
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
                        inactive-text="М">
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
                <el-switch v-model="scope.row.prof"> </el-switch>
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

        <el-collapse-transition>
        <div id="adding" v-show="show">
        <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="ФИО">
                <el-input v-model="form.pib"> </el-input>
            </el-form-item>
            <el-form-item label="Группа">
                    <el-input v-model="form.group"> </el-input>
            </el-form-item>
            <el-form-item label="Пол">
                <el-switch
                        style="display: block"
                        v-model="form.gender"
                        active-color="#F000D7"
                        inactive-color="#00A9F9"
                        active-text="Женский"
                        inactive-text="Мужской">
                </el-switch>
            </el-form-item>
            <el-form-item label="Идентификационный Код" label-width="200px">
                <el-input v-model="form.code"> </el-input>
            </el-form-item>
            <el-form-item label="Дата рождения">
                <el-col :span="11">
                    <el-date-picker type="date" placeholder="Pick a date" v-model="form.date" style="width: 100%;"> </el-date-picker>
                </el-col>
            </el-form-item>
            <el-form-item label="Адрес">
                <el-input v-model="form.address"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit()">Create</el-button>
                <el-button @click="hide()">Cancel</el-button>
            </el-form-item>
        </el-form>
    </div>
        </el-collapse-transition>
    </div>

</template>

<script>
    import { allStudents,  deleteStudent, addStudent, /*getDoc*/ } from '../api.js';
    export default {
        name: "StudentTable",
        mounted() {
            /*getDoc().then(res=> res.data.forEach(el=>{
                addStudent(
                    el.name,
                    "test",
                    true,
                    el.code,
                    "0000-00-00 00:00:00",
                    "test")
            }));*/
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
            updateTab(){
                this.tableData = [];
                allStudents().then(res => res.data.forEach(el=>{
                    var dat = el.date.split("T");
                    const val = {
                        idStud: el.idStud,
                        prof: el.prof,
                        pib:el.pib,
                        group:el.group,
                        gender:el.gender,
                        code:el.code,
                        date: dat[0],
                        address:el.address};

                    this.tableData.push(val)}));
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
            add(){
                this.show=true;
            },
            hide(){
                    this.form.pib= '';
                this.form.group= '';
                this.form.gender=false;
                this.form.code='';
                this.form.date= '';
                this.form.address='';
                this.show=false;
            },
            onSubmit(){
                addStudent(
                    this.form.pib,
                    this.form.group,
                    this.form.gender,
                    this.form.code,
                    this.form.date,
                    this.form.address).then(res=>{
                        if (res.data!=null){
                            this.form.pib= '';
                            this.form.group= '';
                            this.form.gender=false;
                            this.form.code='';
                            this.form.date= '';
                            this.form.address='';
                            this.show=false;
                            this.updateTab();
                        }
                        else{
                            alert("error!");
                        }
                });

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
                console.log(index, row);
            }

        },
        data() {
            return {
                form: {
                    pib: '',
                    group: '',
                    gender:false,
                    code:'',
                    date: '',
                   address:''
                },
                tableData: [],
                show:false,
                search: ''
            }
        }
    }

</script>

<style scoped>
#adding{
    position: fixed;
    width: 50vw;
    top: 40px;
    left: 25vw;
    background-color: lightblue;
    border: 1px solid blue;
    border-radius: 8px;
    /*background-color: black;*/
}
</style>