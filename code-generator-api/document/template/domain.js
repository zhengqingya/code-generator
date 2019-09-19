import axios from '@/libs/api.request'

export const getDataListPage = (filter) => {
    return axios.request({
        url: '/api/crane/${domain}/listPage',
        data: filter,
        method: 'post'
    })
};

export const getDataList = (query) => {
    return axios.request({
        url: '/api/crane/${domain}/list',
        data: {},
        method: 'post'
    })
};

export const saveOrUpdateData = (form) => {
    return axios.request({
        url: '/api/crane/${domain}/save',
        data: form,
        method: 'post'
    })
};

export const deleteData = (id) => {
    return axios.request({
        url: '/api/crane/${domain}/delete',
        data: id,
        method: 'post'
    })
};
