"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { Usuario } from "@/app/types/usuario";
import {
    buscarListaUsuarios, deletarUsuario,
    atualizarStatusUsuario, atualizarRoleUsuario
} from "@/app/services/usuarioService";

export default function Usuarios() {
    const [usuarios, setUsuarios] = useState<Usuario[]>([]);

    const carregarDados = async () => {
        try {
            setUsuarios(await buscarListaUsuarios());
        } catch (error) {
            alert("Erro ao carregar usuários!");
        }
    };

    useEffect(() => {
        carregarDados();
    }, []);

    const excluir = async (usuario: Usuario) => {
        if (usuario.id == null || !window.confirm("Deseja excluir este usuário?")) return;
        try {
            await deletarUsuario(usuario.id);
            await carregarDados();
        } catch (error) {
            alert("Erro ao excluir usuário!");
        }
    };

    const alterarStatus = async (usuario: Usuario) => {
        try {
            await atualizarStatusUsuario(usuario);
            await carregarDados();
        } catch (error) {
            alert("Erro ao atualizar status!");
        }
    };

    const alterarRole = async (usuario: Usuario, role: string) => {
        if (usuario.id == null) return;
        try {
            await atualizarRoleUsuario(usuario.id, role);
            await carregarDados();
        } catch (error) {
            alert("Erro ao atualizar perfil!");
        }
    };

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-7xl">
                <div className="mb-8 flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-purple-800">Gestão de usuários</h1>
                    <Link href="/usuarios/novo"
                        className="rounded-lg bg-purple-700 px-5 py-3 text-white">
                        Novo usuário
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
                                <th className="p-4">Perfil</th>
                                <th className="p-4">Status</th>
                                <th className="p-4">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuarios.map(usuario => (
                                <tr key={usuario.id} className="border-b border-purple-100">
                                    <td className="p-4">{usuario.id}</td>
                                    <td className="p-4">{usuario.nome}</td>
                                    <td className="p-4">{usuario.cpf}</td>
                                    <td className="p-4">{usuario.email}</td>
                                    <td className="p-4">
                                        <select value={usuario.role || "ROLE_RECEPCAO"}
                                            onChange={e => alterarRole(usuario, e.target.value)}
                                            className="rounded-lg border border-purple-200 p-2">
                                            <option value="ROLE_ADMIN">Administrador</option>
                                            <option value="ROLE_FISIOTERAPEUTA">Fisioterapeuta</option>
                                            <option value="ROLE_RECEPCAO">Recepção</option>
                                        </select>
                                    </td>
                                    <td className="p-4">{usuario.status}</td>
                                    <td className="p-4">
                                        <Link href={`/usuarios/${usuario.id}/editar`} className="mr-3 text-purple-700">Editar</Link>
                                        <button onClick={() => alterarStatus(usuario)}
                                            className="mr-3 text-orange-700">Status</button>
                                        <button onClick={() => excluir(usuario)} className="text-red-700">Excluir</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {usuarios.length === 0 && <p className="p-6 text-gray-600">Nenhum usuário encontrado.</p>}
                </div>
            </div>
        </div>
    );
}
