"use client";

import { Tratamento, TratamentoFormProps } from "@/app/types/tratamento";
import type { Paciente } from "@/app/types/paciente";
import { listarPacientes } from "@/app/services/pacienteService";
import { salvarTratamento, atualizarTratamento } from "@/app/services/tratamentoService";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function TratamentoForm({ tratamentoExistente }: TratamentoFormProps) {
    const router = useRouter();
    const [tratamento, setTratamento] = useState<Tratamento>(
        tratamentoExistente || new Tratamento(null, "", "", "", "", 0, 0, "ATIVO")
    );
    const [pacientes, setPacientes] = useState<Paciente[]>([]);

    useEffect(() => {
        listarPacientes().then(setPacientes).catch(() => alert("Erro ao buscar pacientes!"));
    }, []);

    const alterar = (campo: keyof Tratamento, valor: string | number) => {
        setTratamento({ ...tratamento, [campo]: valor });
    };

    const salvar = async (formData: FormData) => {
        try {
            if (tratamento.pacienteId == null) {
                alert("Selecione um paciente!");
                return;
            }
            if (tratamentoExistente) {
                await atualizarTratamento(tratamento);
            } else {
                await salvarTratamento(tratamento);
            }
            alert("Tratamento salvo com sucesso!");
            router.push("/tratamentos");
        } catch (error) {
            alert("Erro ao salvar tratamento! Verifique os dados.");
        }
    };

    return (
        <form action={salvar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label className="block mb-2 text-purple-800">Paciente</label>
                    <select required value={tratamento.pacienteId ?? ""}
                        onChange={e => alterar("pacienteId", Number(e.target.value))}
                        className="w-full rounded-lg border border-purple-200 p-3 text-gray-800">
                        <option value="">Selecione o paciente</option>
                        {pacientes.map(paciente => (
                            <option key={paciente.id} value={paciente.id ?? ""}>{paciente.nome}</option>
                        ))}
                    </select>
                </div>
                {([
                    ["nome", "Nome", "text"],
                    ["descricao", "Descrição", "text"],
                    ["dataInicio", "Data de início", "datetime-local"],
                    ["dataFinal", "Data final", "datetime-local"],
                    ["totalSessoes", "Total de sessões", "number"],
                    ["sessoesRealizadas", "Sessões realizadas", "number"]
                ] as const).map(([campo, titulo, tipo]) => (
                    <div key={campo}>
                        <label className="block mb-2 text-purple-800">{titulo}</label>
                        <input type={tipo} required={campo !== "descricao"} min={tipo === "number" ? 0 : undefined}
                            value={tipo === "datetime-local" ? String(tratamento[campo]).slice(0, 16) : tratamento[campo]}
                            onChange={e => alterar(campo, tipo === "number" ? Number(e.target.value) : e.target.value)}
                            className="w-full rounded-lg border border-purple-200 p-3 text-gray-800" />
                    </div>
                ))}
            </div>
            <div className="flex justify-end gap-4">
                <Link href="/tratamentos" className="rounded-lg bg-purple-100 px-5 py-3 text-purple-800">Cancelar</Link>
                <button type="submit" className="rounded-lg bg-purple-700 px-5 py-3 text-white">Salvar</button>
            </div>
        </form>
    );
}
