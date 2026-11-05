import api from "./api";
import type { Paciente } from "../types/paciente";

export async function listarPacientes(): Promise<Paciente[]> {
    return (await api.get<Paciente[]>("/pacientes")).data;
}
export async function buscarPacientePorId(id: number): Promise<Paciente> {
    return (await api.get<Paciente>("/pacientes/" + id)).data;
}
export async function salvarPaciente(dados: Paciente) {
    return (await api.post("/pacientes", dados)).data;
}
export async function atualizarPaciente(dados: Paciente) {
    return (await api.put("/pacientes/" + dados.id, dados)).data;
}
export async function excluirPaciente(id: number) {
    return (await api.delete("/pacientes/" + id + "/excluir")).status;
}
export async function atualizarStatusPaciente(id: number, status: string) {
    return (await api.patch("/pacientes/" + id + "/status", { status })).status;
}

export async function buscarCep(cep: string):
    Promise<{ logradouro: string; bairro: string; localidade: string; uf: string; erro?: string }> {
    return (await api.get("/pacientes/cep/" + cep.split("").filter(c => c >= "0" && c <= "9").join(""))).data;
}
