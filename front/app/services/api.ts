"use client";

import axios from "axios";
import { store } from "@/app/redux/store";
import { logout } from "@/app/redux/slices/authSlice";
import { limparPacientes } from "@/app/redux/slices/pacienteSlice";

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"
});

// O token fica na store do Redux, conforme a avaliação final.
api.interceptors.request.use(config => {
    const token = store.getState().auth.token;
    if (token) {
        config.headers.Authorization = "Bearer " + token;
    }
    return config;
});

// Se o token expirar, o sistema volta para o login.
api.interceptors.response.use(
    resposta => resposta,
    erro => {
        if (erro.response?.status === 401) {
            store.dispatch(logout());
            store.dispatch(limparPacientes());
            if (typeof window !== "undefined" && window.location.pathname !== "/login") {
                window.location.assign("/login");
            }
        }
        return Promise.reject(erro);
    }
);

export default api;
