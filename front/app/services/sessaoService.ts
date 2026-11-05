import api from "./api";
import type { Sessao } from "../types/sessao";

export async function listarSessoes(): Promise<Sessao[]> {
    return (await api.get<Sessao[]>("/sessoes")).data;
}
export async function buscarSessaoPorId(id: number): Promise<Sessao> {
    return (await api.get<Sessao>("/sessoes/" + id)).data;
}
export async function salvarSessao(dados: Sessao) {
    return (await api.post("/sessoes", dados)).data;
}
export async function atualizarSessao(dados: Sessao) {
    return (await api.put("/sessoes/" + dados.id, dados)).data;
}
export async function excluirSessao(id: number) {
    return (await api.delete("/sessoes/" + id + "/excluir")).status;
}
export async function atualizarStatusSessao(id: number, status: string) {
    return (await api.patch("/sessoes/" + id + "/status", { status })).status;
}
