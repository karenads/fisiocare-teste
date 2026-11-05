"use client";

import { Sessao, SessaoFormProps } from "@/app/types/sessao";
import type { Tratamento } from "@/app/types/tratamento";
import { listarTratamentos } from "@/app/services/tratamentoService";
import { salvarSessao, atualizarSessao } from "@/app/services/sessaoService";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function SessaoForm({ sessaoExistente }: SessaoFormProps) {
    const router = useRouter();
    const [sessao, setSessao] = useState<Sessao>(
        sessaoExistente || new Sessao(null, "", "", "", "", false, "ATIVO")
    );
    const [tratamentos, setTratamentos] = useState<Tratamento[]>([]);

    useEffect(() => {
        listarTratamentos().then(setTratamentos).catch(() => alert("Erro ao buscar tratamentos!"));
    }, []);

    const alterar = (campo: keyof Sessao, valor: string | number | boolean) => {
        setSessao({ ...sessao, [campo]: valor });
    };

    const salvar = async (formData: FormData) => {
        try {
            if (sessao.tratamentoId == null) {
                alert("Selecione um tratamento!");
                return;
            }
            if (sessaoExistente) {
                await atualizarSessao(sessao);
            } else {
                await salvarSessao(sessao);
            }
            alert("Sessão salva com sucesso!");
            router.push("/sessoes");
        } catch (error) {
            alert("Erro ao salvar sessão! Verifique os dados.");
        }
    };

    return (
        <form action={salvar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label className="block mb-2 text-purple-800">Tratamento</label>
                    <select required value={sessao.tratamentoId ?? ""}
                        onChange={e => alterar("tratamentoId", Number(e.target.value))}
                        className="w-full rounded-lg border border-purple-200 p-3 text-gray-800">
                        <option value="">Selecione o tratamento</option>
                        {tratamentos.map(tratamento => (
                            <option key={tratamento.id} value={tratamento.id ?? ""}>{tratamento.nome}</option>
                        ))}
                    </select>
                </div>
                {([
                    ["data", "Data", "date"],
                    ["horario", "Horário", "time"],
                    ["descricao", "Descrição", "text"],
                    ["observacoes", "Observações", "text"]
                ] as const).map(([campo, titulo, tipo]) => (
                    <div key={campo}>
                        <label className="block mb-2 text-purple-800">{titulo}</label>
                        <input type={tipo} required={campo === "data" || campo === "horario"}
                            value={sessao[campo]}
                            onChange={e => alterar(campo, e.target.value)}
                            className="w-full rounded-lg border border-purple-200 p-3 text-gray-800" />
                    </div>
                ))}
                <label className="flex items-center gap-3 text-purple-800">
                    <input type="checkbox" checked={sessao.realizada}
                        onChange={e => alterar("realizada", e.target.checked)} />
                    Sessão realizada
                </label>
            </div>
            <div className="flex justify-end gap-4">
                <Link href="/sessoes" className="rounded-lg bg-purple-100 px-5 py-3 text-purple-800">Cancelar</Link>
                <button type="submit" className="rounded-lg bg-purple-700 px-5 py-3 text-white">Salvar</button>
            </div>
        </form>
    );
}
