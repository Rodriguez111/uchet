let indocsControllerGetAll = Vue.resource('../incoming_docs_controller/all');
let indocsControllerGetFiltered = Vue.resource('../incoming_docs_controller/local_filter');
let indocsControllerGetByGlobalFilter = Vue.resource('../incoming_docs_controller/global_filter');
let indocsControllerGetByDocNumber = Vue.resource('../incoming_docs_controller/{docNumber}');


Vue.component('app-indocs', {
    props: [
        'listOfRows',
        'tableUpdateToggle'
    ],
    template: `<div>
        <tr class="main_table_tr" v-for="row in listOfRows" @dblclick="showDetail(row.docNumber)">
        <td class="main_table_td column_1">{{row.docNumber}}</td>
        <td class="main_table_td column_2">{{row.docDate}}</td>
        <td class="main_table_td column_3">{{row.contractor.name}}</td>
        <td class="main_table_td column_4">{{row.type.docType}} </td>
        <td class="main_table_td column_5">{{row.owner.fullName}} </td>
        <td class="main_table_td column_6">{{row.details.length}}</td>       
        </tr>        
        </div>`,
    created: function () {
        // indocsControllerGetAll.get().then(result => result.json().then(data => data.forEach(row => {
        //         this.listOfRows.push(row)
        //     }
        // )))
    },
    watch: {
        tableUpdateToggle: function () {
            inDocsApp.listOfRows = [];
            indocsControllerGetAll.get().then(result => result.json().then(data => data.forEach(row => {
                    this.listOfRows.push(row)
                }
            )))
        }
    },
    methods: {
        showUpdatePopup(skuId) {
            inDocsApp.showUpdatePopup(skuId)
        },
        showDeletePopup(skuId) {
            inDocsApp.showDeletePopup(skuId)
        },
        showDetail(docNumber) {
            window.location.href = contextPath + '/accounting/incoming_doc_details/' + docNumber;
        }
    }

});












var inDocsApp = new Vue({
    el: '#indocs-app',
    data() {
        return {
            listOfRows: [],
            listOfOwners: [],
            listOfContractors: [],
            isAddPopupVisible: false,
            isGlobalFilterPopupVisible:false,
            newObject: {},
            codeErrorEnable: false,
            nameErrorEnable: false,
            contractorsErrorEnable: false,
            pkgErrorEnable: false,
            error_message: '',
            toggleForUpdate: true,
        }
    },
    methods: {
        applyGlobalFilter(globalFilterParams) {
            this.listOfRows = [];
            indocsControllerGetByGlobalFilter.save(globalFilterParams).then(result => result.json().then(data => data.forEach(row => {
                this.listOfRows.push(row);
                }
            )));
            this.isGlobalFilterPopupVisible = false;
        },
        showGlobalFilterPopup() {
            this.isGlobalFilterPopupVisible = true;
        },
        hideGlobalFilterWindow() {
            this.isGlobalFilterPopupVisible = false;
        },
        showAddPopup() {
            this.resetNewObject();
            this.isAddPopupVisible = true;
        },
        showUpdatePopup(skuId) {
            this.initObjectWithFieldsForUpdateOrDelete(skuId);
            this.isUpdatePopupVisible = true;
        },
        showDeletePopup(skuId) {
            this.initObjectWithFieldsForUpdateOrDelete(skuId);
            this.isDeletePopupVisible = true;
        },
        initObjectWithFieldsForUpdateOrDelete(skuId) {
            var sku = this.findSkuById(skuId);
            this.newObject = _.cloneDeep(sku); //клонируем объект методом библиотеки lodash
        },
        closeAddPopupWindow() {
            this.resetErrorStyles();
            this.isAddPopupVisible = false;

        },
        closeUpdatePopupWindow() {
            this.resetErrorStyles();
            this.isUpdatePopupVisible = false;
        },
        closeDeletePopupWindow() {
            this.resetErrorStyles();
            this.isDeletePopupVisible = false;
        },
        validateAndAdd() {
            let validateResult = this.validateAdd();
            if (validateResult) {
                this.sendAddData();
            }
        },
        validateAndUpdate() {
            let validateResult = this.validateUpdate();
            if (validateResult) {
                this.sendDataForUpdate();
            }
        },
        validateAdd() {
            let result = true;
            this.resetErrorStyles();
            if (!this.checkCodeField(this.newObject.code)) {
                this.error_message = "Код товара - обязательное поле, и он должен состоять из 6 цифр и быть больше 0";
                this.codeErrorEnable = true;
                result = false;
            } else if (!this.checkLength(this.newObject.name, 2, 255)) {
                this.error_message = "Наименование товара - обязательное поле, от 2 до 255 символов";
                this.nameErrorEnable = true;
                result = false;
            } else if (!this.checkContractorField(this.newObject.contractor)) {
                this.error_message = "Нужно выбрать поставщика";
                this.contractorsErrorEnable = true;
                result = false;
            } else if (!this.checkPkg(this.newObject.quantityInSecondaryPackaging)) {
                this.error_message = "Значение количества во вторичной упаковке должно быть числом больше 0";
                this.pkgErrorEnable = true;
                result = false;
            }
            return result;
        },
        validateUpdate() {
            let result = true;
            this.resetErrorStyles();
            if (!this.checkCodeField(this.newObject.code)) {
                this.error_message = "Код товара должен состоять из 6 цифр и быть больше 0";
                this.codeErrorEnable = true;
                result = false;
            } else if (this.newObject.name.length > 0 && !this.checkLength(this.newObject.name, 2, 255)) {
                this.error_message = "Наименование товара - может быть от 2 до 255 символов, или оставьте это поле пустым";
                this.nameErrorEnable = true;
                result = false;
            } else if (!this.checkPkg(this.newObject.quantityInSecondaryPackaging)) {
                this.error_message = "Значение количества во вторичной упаковке должно быть числом больше 0";
                this.pkgErrorEnable = true;
                result = false;
            }
            return result;
        },
        sendDataForDelete() {
            skuController.delete({id: this.newObject.id})
                .then(result => result.json().then(data => {
                    let deleteResult = data.deleteResult;
                    if (deleteResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeDeletePopupWindow();
                    } else {
                        this.error_message = deleteResult;
                    }
                }));
        },
        checkLength(string, minLength, maxLength) {
            let result = true;
            if (string == null) { //проверка на undefined
                result = false;
            } else if (string.length < minLength || string.length > maxLength) {
                result = false;
            }
            return result;
        },
        checkCodeField(string) {
            let result = string != null;
            if (result) {
                let regex = new RegExp("^(\\d{6})$");
                result = regex.test(string) && parseInt(string) > 0;
            }
            return result;
        },
        checkContractorField(string) {
            return string != null;
        },
        checkPkg(string) {
            let regex = new RegExp("^(\\d{1,9})$");
            return regex.test(string) && parseInt(string) > 0;
        },
        resetErrorStyles() {
            this.error_message = '';
            this.codeErrorEnable = false;
            this.nameErrorEnable = false;
            this.contractorsErrorEnable = false;
            this.pkgErrorEnable = false
        },
        resetNewObject() {
            this.newObject = {
                code:"",
                name:"",
                contractor:{},
                active: true,
                quantityInSecondaryPackaging: 1
            };
        },
        findSkuById(id) {
            var foundSku;
            for (var i in this.listOfRows) {
                if (this.listOfRows[i].id === id) {
                    foundSku = this.listOfRows[i];
                    break;
                }
            }
            return foundSku;
        },
        sendAddData() {
            skuController.save(this.newObject)
                .then(result => result.json().then(data => {
                    let addResult = data.addResult;
                    if (addResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeAddPopupWindow();
                    } else {
                        this.error_message = addResult;
                    }
                }));
        },
        sendDataForUpdate() {
            skuController.update(this.newObject)
                .then(result => result.json().then(data => {
                    let updateResult = data.updateResult;
                    if (updateResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeUpdatePopupWindow();
                    } else {
                        this.error_message = updateResult;
                    }
                }));
        },
    },
    computed: {
        countOfRows() {
            return this.listOfRows.length
        },
        sumOfPositions() {
            var sum = 0;
            for(var i = 0; i < this.listOfRows.length; i++) {
                sum += this.listOfRows[i].details.length;
            }
            return sum;
        },
    },
    components: {

    }
});

