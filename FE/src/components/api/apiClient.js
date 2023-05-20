import axios from "axios";


export const client =axios.create(
    {
        baseURL: "http://localhost:8080"
    }
)

export const apiClient =axios.create(
    {
       baseURL: "http://localhost:8080/api/v1"
    }
)

export const getAuth 
    = (token) => apiClient.get(`/auth`)



export const getRegister
= (username) => apiClient.get(`/exercises`, {
    headers: {
        Authorization: "Basic bmFtZTpwYXNzd29yZA=="
    }
})

export const getAllPlansForUserApi
= () => apiClient.get(`/plans`, {
    headers: {
        Authorization: "Basic bmFtZTpwYXNzd29yZA=="
    }
})

export const deletePlanByIdApi
= (id) => apiClient.delete(`/plans/${id}`)


export const getExerciseApi
= () => apiClient.get(`/exercises`)

export const getExerciseByIdApi
= (idE) => apiClient.get(`/exercises/${idE}`)

export const getPlanByIdApi
= (id) => apiClient.get(`/plans/${id}`)

export const patchPlanByIdApi
= (id, plan) => apiClient.patch(`/plans/${id}`, plan)

export const postPlanByIdApi
= (plan) => apiClient.post(`/plans`, plan)

export const getTreningsApi
= (id) => apiClient.get(`/plans/${id}/trenings`)

export const deleteTreningsApi
= (id, idT) => apiClient.delete(`/plans/${id}/trenings/${idT}`)

export const getTreningByIdApi
= (id, idT) => apiClient.get(`/plans/${id}/trenings/${idT}`)

export const patchTreningByIdApi
= (id, idT, trening) => apiClient.patch(`/plans/${id}/trenings/${idT}`, trening)

export const postTreningByIdApi
= (id, trening) => apiClient.post(`/plans/${id}/trenings`, trening)

export const getExercisesApi
= (id, idT) => apiClient.get(`plans/${id}/trenings/${idT}/exercises`)

export const getTreningExercisesByIdApi
= (id, idT,idTE) => apiClient.get(`plans/${id}/trenings/${idT}/exercises/${idTE}`)

export const postExercisesApi
= (id, idT, exercise) => apiClient.post(`plans/${id}/trenings/${idT}/exercises`, exercise)

export const deleteExercisesByIdApi
= (id, idT, idE) => apiClient.delete(`plans/${id}/trenings/${idT}/exercises/${idE}`)

export const patchExercisesByIdApi
= (id, idT, idE, exercise) => apiClient.patch(`plans/${id}/trenings/${idT}/exercises/${idE}`, exercise)



