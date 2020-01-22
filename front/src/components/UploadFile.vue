<template>
    <div id="app">
        <br>Выберите факультет для файлов
        <el-select v-model="value" placeholder="Select" @change="selChange">
            <el-option
                    v-for="item in facultyData"
                    :key="item.facId"
                    :label="item.abbreviation"
                    :value="item.facId"
                    >
            </el-option>
        </el-select>

        <el-upload
                class="upload"
                ref="upload"
                :action="url"
                :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">select file</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">upload to server</el-button>
            <div class="el-upload__tip" slot="tip">jpg/png files with a size less than 500kb</div>
        </el-upload>



    </div>
</template>

<script>
   // import {sendDoc} from "../api";

    export default {
        name: "UploadFile",
        props:{
            facultyData:[]
        },
        methods: {
            submitUpload() {
                if (this.value!==''){
                    this.url = "http://localhost:7000/api/files/"+this.value;
                    this.$refs.upload.submit();
                }
                else {
                    this.$message.error('Выберите факультет!');
                }


            },
            selChange(){
                this.url = "http://localhost:7000/api/files/"+this.value;
                console.log(this.value);
            }
           /* requestHandle(param){
                console.log(param);
                var file = param.file;
                sendDoc(file).then(res => alert(res));
            }*/

        },
        data(){
            return{
                value:'',
                url:''
            }
        }
    }
</script>

<style scoped>

</style>