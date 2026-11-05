"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import type { Tratamento } from "@/app/types/tratamento";
import { listarTratamentos, excluirTratamento } from "@/app/services/tratamentoService";


export default function Tratamentos() {
    
    const [dados, setDados] = useState<Tratamento[]>([]);

    const carregarDados = async () => {
        try {
            setDados(await listarTratamentos());
        } catch (error) {
            alert("Erro ao carregar tratamentos!");
        }
    };

    useEffect(() => {
        carregarDados();
        // Busca inicial dos registros
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleExcluir = async (item: Tratamento) => {
        if (item.id == null || !window.confirm("Deseja excluir este registro?")) return;
        try {
            await excluirTratamento(item.id);
            await carregarDados();
        } catch (error) {
            alert("Erro ao excluir!");
        }
    };

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-7xl">
                <div className="mb-8 flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-purple-800">Tratamentos</h1>
                    <Link href="/tratamentos/novo"
                        className="rounded-lg bg-purple-700 px-5 py-3 font-semibold text-white">
                        Novo cadastro
                    </Link>
                </div>
                <div className="overflow-x-auto rounded-xl bg-white shadow-sm">
                    <table className="w-full text-left text-gray-800">
                        <thead className="bg-purple-100">
                            <tr>
                                <th className="p-4">Código</th>
                                <th className="p-4">Nome</th>
                                <th className="p-4">Paciente</th>
                                <th className="p-4">Total de sessões</th>
                                <th className="p-4">Status</th>
                                <th className="p-4">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {dados.map(item => (
                                <tr key={item.id} className="border-b border-purple-100">
                                    <td className="p-4">{item.id}</td>
                                    <td className="p-4">{item.nome}</td>
                                    <td className="p-4">{item.pacienteId}</td>
                                    <td className="p-4">{item.totalSessoes}</td>
                                    <td className="p-4">{item.status}</td>
                                    <td className="p-4">
                                        <Link className="mr-4 text-purple-700"
                                            href={`/tratamentos/${item.id}/editar`}>Editar</Link>
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
