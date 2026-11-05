"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import type { Sessao } from "@/app/types/sessao";
import { listarSessoes, excluirSessao } from "@/app/services/sessaoService";


export default function Sessaos() {
    
    const [dados, setDados] = useState<Sessao[]>([]);

    const carregarDados = async () => {
        try {
            setDados(await listarSessoes());
        } catch (error) {
            alert("Erro ao carregar sessoes!");
        }
    };

    useEffect(() => {
        carregarDados();
        // Busca inicial dos registros
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleExcluir = async (item: Sessao) => {
        if (item.id == null || !window.confirm("Deseja excluir este registro?")) return;
        try {
            await excluirSessao(item.id);
            await carregarDados();
        } catch (error) {
            alert("Erro ao excluir!");
        }
    };

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-7xl">
                <div className="mb-8 flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-purple-800">Sessoes</h1>
                    <Link href="/sessoes/nova"
                        className="rounded-lg bg-purple-700 px-5 py-3 font-semibold text-white">
                        Novo cadastro
                    </Link>
                </div>
                <div className="overflow-x-auto rounded-xl bg-white shadow-sm">
                    <table className="w-full text-left text-gray-800">
                        <thead className="bg-purple-100">
                            <tr>
                                <th className="p-4">Código</th>
                                <th className="p-4">Data</th>
                                <th className="p-4">Horário</th>
                                <th className="p-4">Tratamento</th>
                                <th className="p-4">Status</th>
                                <th className="p-4">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {dados.map(item => (
                                <tr key={item.id} className="border-b border-purple-100">
                                    <td className="p-4">{item.id}</td>
                                    <td className="p-4">{item.data}</td>
                                    <td className="p-4">{item.horario}</td>
                                    <td className="p-4">{item.tratamentoId}</td>
                                    <td className="p-4">{item.status}</td>
                                    <td className="p-4">
                                        <Link className="mr-4 text-purple-700"
                                            href={`/sessoes/${item.id}/editar`}>Editar</Link>
                                        <button className="text-red-700" onClick={() => handleExcluir(item)}>
                                            Excluir
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {dados.length === 0 && <p className="p-6 text-gray-600">Nenhum registro encontrado.</p>}
                </div>
            </div>
        </div>
    );
}
