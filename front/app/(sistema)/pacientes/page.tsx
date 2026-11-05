"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import type { Paciente } from "@/app/types/paciente";
import { listarPacientes, excluirPaciente } from "@/app/services/pacienteService";
import { useSelector, useDispatch } from "react-redux";
import type { RootState } from "@/app/redux/store";
import { definirPacientes } from "@/app/redux/slices/pacienteSlice";

export default function Pacientes() {
    
    const dados = useSelector((state: RootState) => state.pacientes);
    const dispatch = useDispatch();

    const carregarDados = async () => {
        try {
            const lista = await listarPacientes();
            dispatch(definirPacientes(lista));
        } catch (error) {
            alert("Erro ao carregar pacientes!");
        }
    };

    useEffect(() => {
        carregarDados();
        // Busca inicial dos registros
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleExcluir = async (item: Paciente) => {
        if (item.id == null || !window.confirm("Deseja excluir este registro?")) return;
        try {
            await excluirPaciente(item.id);
            await carregarDados();
        } catch (error) {
            alert("Erro ao excluir!");
        }
    };

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-7xl">
                <div className="mb-8 flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-purple-800">Pacientes</h1>
                    <Link href="/pacientes/novo"
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
                                <th className="p-4">CPF</th>
                                <th className="p-4">E-mail</th>
                                <th className="p-4">Status</th>
                                <th className="p-4">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {dados.map(item => (
                                <tr key={item.id} className="border-b border-purple-100">
                                    <td className="p-4">{item.id}</td>
                                    <td className="p-4">{item.nome}</td>
                                    <td className="p-4">{item.cpf}</td>
                                    <td className="p-4">{item.email}</td>
                                    <td className="p-4">{item.status}</td>
                                    <td className="p-4">
                                        <Link className="mr-4 text-purple-700"
                                            href={`/pacientes/${item.id}/editar`}>Editar</Link>
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
