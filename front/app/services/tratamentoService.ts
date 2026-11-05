import api from "./api";
import type { Tratamento } from "../types/tratamento";

export async function listarTratamentos(): Promise<Tratamento[]> {
    return (await api.get<Tratamento[]>("/tratamentos")).data;
}
export async function buscarTratamentoPorId(id: number): Promise<Tratamento> {
    return (await api.get<Tratamento>("/tratamentos/" + id)).data;
}
export async function salvarTratamento(dados: Tratamento) {
    return (await api.post("/tratamentos", dados)).data;
}
export async function atualizarTratamento(dados: Tratamento) {
    return (await api.put("/tratamentos/" + dados.id, dados)).data;
}
export async function excluirTratamento(id: number) {
    return (await api.delete("/tratamentos/" + id + "/excluir")).status;
}
export async function atualizarStatusTratamento(id: number, status: string) {
    return (await api.patch("/tratamentos/" + id + "/status", { status })).status;
}
