var table_header_comp = Vue.component('app-table-header-skuin', {
    template: `
     <tr>
                <th class="column_1"><a href="#" class="menu_title_link" @click="sort('column01')">N п/п<div :class="sortIconSelector('column01')"></div></a></th>
                <th class="column_2"><a href="#" class="menu_title_link" @click="sort('column02')">Партия<div :class="sortIconSelector('column02')"></div></a></th>
                <th class="column_3"><a href="#" class="menu_title_link" @click="sort('column03')">Код тов.<div :class="sortIconSelector('column03')"></div></a></th>
                 <th class="column_4"><a href="#" class="menu_title_link" @click="sort('column04')">Наименование товара<div :class="sortIconSelector('column04')"></div></a></th>
                <th class="column_5"><a href="#" class="menu_title_link" @click="sort('column05')">Серия<div :class="sortIconSelector('column05')"></div></a></th>
                <th class="column_6"><a href="#" class="menu_title_link" @click="sort('column06')">Срок годности<div :class="sortIconSelector('column06')"></div></a></th>
                <th class="column_7"><a href="#" class="menu_title_link" @click="sort('column07')">Количество<div :class="sortIconSelector('column07')"></div></a></th>
                <th class="column_8"><a href="#" class="menu_title_link" @click="sort('column08')">Ед. изм.<div :class="sortIconSelector('column08')"></div></a></th>
                <th class="column_9"><a href="#" class="menu_title_link" @click="sort('column09')">Цена<div :class="sortIconSelector('column09')"></div></a></th>
                <th class="column_10"><a href="#" class="menu_title_link" @click="sort('column010')">Цена с НДС<div :class="sortIconSelector('column010')"></div></a></th>
                <th class="column_11"><a href="#" class="menu_title_link" @click="sort('column11')">Сумма<div :class="sortIconSelector('column11')"></div></a></th>
                <th class="column_12"><a href="#" class="menu_title_link" @click="sort('column12')">Сумма с НДС<div :class="sortIconSelector('column12')"></div></a></th>                
                <th class="column_13"><a href="#" class="menu_title_link" @click="sort('column13')">Дата прихода<div :class="sortIconSelector('column13')"></div></a></th>
                <th class="column_14"><a href="#" class="menu_title_link" @click="sort('column14')">Поставщик<div :class="sortIconSelector('column14')"></div></a></th>
                <th class="column_15"><a href="#" class="menu_title_link" @click="sort('column15')">Тип<div :class="sortIconSelector('column15')"></div></a></th>
                <th class="column_16"><a href="#" class="menu_title_link" @click="sort('column16')">Кто принял<div :class="sortIconSelector('column16')"></div></a></th>
                              
            </tr>    
    `,
    data() {
        return {
            currentSort:'',
            currentSortDir:''
        }
    },
    methods: {
        sort(columnName) {

            if(columnName === this.currentSort) {
                this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
            } else {
                this.currentSortDir =  'asc';
            }
            this.currentSort = columnName;
            var sorted = inDocsApp.listOfRows.sort(this.comparator);
            inDocsApp.listOfRows = sorted;
        },
        comparator(o1, o2) {
            var result = 0;
            if(this.currentSort === 'column01'){
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.docNumber, o2.docNumber);
                } else {
                    result = this.compareStringsDesc(o1.docNumber, o2.docNumber);
                }
            } else if(this.currentSort === 'column02') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.docDate, o2.docDate);
                } else {
                    result = this.compareStringsDesc(o1.docDate, o2.docDate);
                }
            } else if(this.currentSort === 'column03') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.contractor.name, o2.contractor.name);
                } else {
                    result = this.compareStringsDesc(o1.contractor.name, o2.contractor.name);
                }
            } else if(this.currentSort === 'column04') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.type.doctype, o2.type.doctype);
                } else {
                    result = this.compareStringsDesc(o2.type.doctype, o2.type.doctype);
                }
            }
            else if(this.currentSort === 'column05') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.owner.fullName, o2.owner.fullName);
                } else {
                    result = this.compareStringsDesc(o2.owner.fullName, o2.owner.fullName);
                }
            } else if(this.currentSort === 'column06') {
                if(this.currentSortDir === 'asc'){
                    result = o1.details.length - o2.details.length;
                } else {
                    result = o2.details.length - o1.details.length;
                }
            }
            return result;
        },
        compareStringsAsc(string1, string2) {
            string1 = string1.toLowerCase();
            string2 = string2.toLowerCase();
            var result = 0;
            if(string1 < string2) {
                result = -1;
            }
            if(string1 > string2) {
                result = 1;
            }
            return result;
        },
        compareStringsDesc(string1, string2) {
            string1 = string1.toLowerCase();
            string2 = string2.toLowerCase();
            var result = 0;
            if(string1 < string2) {
                result = 1;
            }
            if(string1 > string2) {
                result = -1;
            }
            return result;
        },
        sortIconSelector(fieldName) {
            var result = "arrow_icon_neutral fa fa-sort";
            if (this.currentSortDir === 'asc' && this.currentSort === fieldName) {
                result = "arrow_icon_up fa fa-sort-asc";
            } else if (this.currentSortDir === 'desc' && this.currentSort === fieldName) {
                result = "arrow_icon_down fa fa-sort-desc";
            }
            return result;
        }
    },

});