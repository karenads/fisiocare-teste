import api from "./api";
import type { LoginResponse } from "../types/auth";
import type { Usuario } from "../types/usuario";

export async function loginBack(email: string, senha: string):
    Promise<{ token: string; usuario: Usuario }> {

    const resposta = await api.post<LoginResponse>("/auth/login", { email, senha });
    const token = resposta.data.token;

    // A busca inicial envia o token explicitamente porque ainda não entrou na store.
    const usuario = await api.get<Usuario>("/usuarios/buscarusuariologado", {
        headers: { Authorization: "Bearer " + token }
    });

    return { token, usuario: usuario.data };
}

export async function sairBack() {
    await api.post("/auth/logout");
}
