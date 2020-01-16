<template>
    <div id="app">
        <el-upload
                class="upload-demo"
                ref="upload"
                action="http://localhost:7000/api/files"
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
        methods: {
            submitUpload() {
                this.$refs.upload.submit()
            }
           /* requestHandle(param){
                console.log(param);
                var file = param.file;
                sendDoc(file).then(res => alert(res));
            }*/

        }
    }
</script>

<style scoped>

</style>