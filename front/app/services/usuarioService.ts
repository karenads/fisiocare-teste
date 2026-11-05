import api from "./api";
import type { Usuario } from "../types/usuario";

export async function buscarListaUsuarios(): Promise<Usuario[]> {
    return (await api.get<Usuario[]>("/usuarios")).data;
}
export async function consultaUsuarioPorId(id: number): Promise<Usuario> {
    return (await api.get<Usuario>("/usuarios/" + id)).data;
}
export async function salvarUsuario(usuario: Usuario) {
    return (await api.post("/usuarios", usuario)).data;
}
export async function atualizarUsuario(usuario: Usuario) {
    return (await api.put("/usuarios/" + usuario.id, usuario)).data;
}
export async function deletarUsuario(id: number) {
    return (await api.delete("/usuarios/" + id + "/excluir")).status;
}
export async function atualizarStatusUsuario(usuario: Usuario) {
    const status = usuario.status === "ATIVO" ? "BLOQUEADO" : "ATIVO";
    return (await api.patch("/usuarios/" + usuario.id + "/status", { status })).status;
}
export async function atualizarRoleUsuario(id: number, role: string) {
    return (await api.patch("/usuarios/" + id + "/role", { role })).status;
}
export async function buscarUsuarioLogado(): Promise<Usuario> {
    return (await api.get<Usuario>("/usuarios/buscarusuariologado")).data;
}
